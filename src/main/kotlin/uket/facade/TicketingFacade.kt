package uket.facade

import org.springframework.stereotype.Service
import uket.common.ErrorLevel
import uket.common.PublicException
import uket.domain.reservation.dto.CreateTicketCommand
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.user.service.UserService
import uket.modules.redis.aop.DistributedLock
import java.time.LocalDateTime

@Service
class TicketingFacade(
    private val userService: UserService,
    private val ticketService: TicketService,
    private val entryGroupService: EntryGroupService,
) {
    @DistributedLock(key = "#entryGroupId")
    fun ticketing(userId: Long, entryGroupId: Long, buyCount: Int, friend: String, at: LocalDateTime): List<Ticket> {
        validateTicketCount(buyCount)

        val entryGroup = entryGroupService.getByIdWithUketEventRoundAndUketEvent(entryGroupId)
        val eventRound = entryGroup.uketEventRound
        validateTicketingDateTime(eventRound, entryGroup, at)

        val user = userService.getById(userId)
        val event = eventRound.uketEvent!!
        validateTicketingCount(user.id, eventRound.id, buyCount, event.buyTicketLimit)

        entryGroupService.increaseReservedCount(entryGroup.id, buyCount)
        return ticketService.publishTickets(CreateTicketCommand(user.id, entryGroup.id, TicketStatus.BEFORE_PAYMENT, friend), buyCount)
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
