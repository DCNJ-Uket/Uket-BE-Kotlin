package uket.facade

import org.springframework.stereotype.Service
import uket.common.ErrorLevel
import uket.common.PublicException
import uket.domain.payment.service.PaymentService
import uket.domain.reservation.dto.CreateTicketCommand
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.uketevent.service.PerformerService
import uket.domain.uketevent.service.UketEventRoundService
import uket.domain.uketevent.service.UketEventService
import uket.domain.user.entity.User
import uket.domain.user.service.UserService
import uket.modules.redis.aop.DistributedLock
import java.time.LocalDateTime

@Service
class TicketingFacade(
    private val userService: UserService,
    private val ticketService: TicketService,
    private val entryGroupService: EntryGroupService,
    private val uketEventRoundService: UketEventRoundService,
    private val uketEventService: UketEventService,
    private val performerService: PerformerService,
    private val ticketingCompletionMessageSendService: TicketingCompletionMessageSendService,
    private val paymentInformationMessageSendService: PaymentInformationMessageSendService,
    private val paymentService: PaymentService,
) {
    @DistributedLock(key = "'ticketing' + #entryGroupId")
    fun ticketing(userId: Long, entryGroupId: Long, buyCount: Int, pName: String, at: LocalDateTime): List<Ticket> {
        validateTicketCount(buyCount)

        val entryGroup = entryGroupService.getById(entryGroupId)
        val eventRound = uketEventRoundService.getById(entryGroup.uketEventRoundId)
        validateTicketingDateTime(eventRound, entryGroup, at)

        val user = userService.getById(userId)
        val event = uketEventService.getById(eventRound.uketEventId)
        validateTicketingCount(user.id, eventRound.id, buyCount, event.buyTicketLimit)

        entryGroupService.increaseReservedCount(entryGroup.id, buyCount)

        val performer = performerService.findByNameAndRoundId(pName, eventRound.id)
        // TODO 지인 별 인원 제한 존재 시 validation 추가 필요
        performerService.addTicketCountForPerformer(performer.id, buyCount)

        val tickets = ticketService.publishTickets(
            createTicketCommand = CreateTicketCommand(
                userId = user.id,
                entryGroupId = entryGroup.id,
                ticketStatus = TicketStatus.init(event.ticketPrice),
                performerName = pName
            ),
            count = buyCount
        )

        sendUserKakaoTalkMessage(tickets, user, event, entryGroup)

        return tickets
    }

    private fun sendUserKakaoTalkMessage(
        tickets: List<Ticket>,
        user: User,
        event: UketEvent,
        entryGroup: EntryGroup,
    ) {
        val ticket = tickets.get(0)
        if (ticket.status == TicketStatus.BEFORE_ENTER) {
            ticketingCompletionMessageSendService.send(
                userName = user.name,
                userPhoneNumber = user.phoneNumber!!,
                eventName = event.eventName,
                eventType = event.eventType,
                ticketNo = ticket.ticketNo,
                eventDate = entryGroup.entryStartDateTime,
                eventLocation = event.location
            )
        } else if (ticket.status == TicketStatus.BEFORE_PAYMENT) {
            val payment = paymentService.getByOrganizationId(event.organizationId)
            paymentInformationMessageSendService.send(
                eventName = event.eventName,
                ticketPrice = event.ticketPrice,
                account = payment.account,
                userName = user.name,
                userPhoneNumber = user.phoneNumber!!
            )
        }
    }

    private fun validateTicketCount(buyCount: Int) {
        check(buyCount > 0) {
            throw IllegalStateException("예매 티켓 개수는 1개 이상이어야 합니다.")
        }
    }

    private fun validateTicketingCount(userId: Long, eventRoundId: Long, buyCount: Int, buyTicketLimit: Int) {
        val activeTickets = ticketService.findAllActiveByUserAndEventRound(userId, eventRoundId)
        if (activeTickets.size + buyCount > buyTicketLimit) {
            throw PublicException(
                publicMessage = "최대 예매 가능한 티켓 개수를 초과했습니다.",
                systemMessage = "[TicketingFacade] 최대 예매 가능 개수($buyTicketLimit) 초과 예매 시도",
                title = "최대 예매 가능 개수($buyTicketLimit) 초과 예매 시도",
                errorLevel = ErrorLevel.WARN
            )
        }
    }

    private fun validateTicketingDateTime(
        uketEventRound: UketEventRound,
        entryGroup: EntryGroup,
        now: LocalDateTime,
    ) {
        if (!uketEventRound.isNowTicketing(now)) {
            throw PublicException(
                publicMessage = "예매가 불가능한 회차입니다.",
                systemMessage = "[TicketingFacade] 예매가 불가능 한 uketEventRound(id=${uketEventRound.id})에 대한 예매 시도",
                title = "예매 불가능한 uketEventRound에 대한 예매 시도",
                errorLevel = ErrorLevel.INFO
            )
        }
        if (entryGroup.entryStartDateTime.isBefore(now)) { // 추후 이벤트 타입 별 수정 필요(축제의 경우 endtime까지도 가능)
            throw PublicException(
                publicMessage = "예매가 불가능한 시간대입니다.",
                systemMessage = "[TicketingFacade] 예매가 불가능한 entryGroup(id=${entryGroup.id})에 대한 예매 시도",
                title = "예매 불가능한 entryGruop에 대한 예매 시도",
                errorLevel = ErrorLevel.INFO
            )
        }
    }
}
