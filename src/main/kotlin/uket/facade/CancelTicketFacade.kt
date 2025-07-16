package uket.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.common.PublicException
import uket.domain.admin.service.OrganizationService
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.uketevent.service.PerformerService
import uket.domain.uketevent.service.UketEventRoundService
import uket.domain.uketevent.service.UketEventService
import uket.domain.user.service.UserService
import uket.facade.messNage.TicketCancelMessageSendService
import uket.facade.message.TicketCancel2MessageSendService
import java.time.LocalDateTime

@Service
class CancelTicketFacade(
    private val ticketService: TicketService,
    private val entryGroupService: EntryGroupService,
    private val uketEventRoundService: UketEventRoundService,
    private val uketEventService: UketEventService,
    private val performerService: PerformerService,
    private val ticketCancelMessageSendService: TicketCancelMessageSendService,
    private val ticketCancel2MessageSendService: TicketCancel2MessageSendService,
    private val userService: UserService,
    private val organizationService: OrganizationService,
) {
    @Transactional
    fun invoke(ticketId: Long, userId: Long, now: LocalDateTime): Ticket {
        ticketService.checkCancelTicketStatus(ticketId)
        ticketService.checkTicketOwner(userId, ticketId)

        val ticket = ticketService.getById(ticketId)
        val entryGroup = entryGroupService.getById(ticket.entryGroupId)
        val eventRound = uketEventRoundService.getById(entryGroup.uketEventRoundId)

        checkTicketingTime(eventRound, now)

        val event = uketEventService.getById(eventRound.uketEventId)

        ticket.cancel(event.isFree())
        entryGroup.decreaseReservedCount();
        reducePerformerTicketCount(ticket, eventRound)

        sendKakaoAlimtalk(ticket, event, userId)

        return ticket
    }

    private fun sendKakaoAlimtalk(
        ticket: Ticket,
        event: UketEvent,
        userId: Long,
    ) {
        println(ticket.status)
        when (ticket.status) {
            TicketStatus.REFUND_REQUESTED -> sendTicketCancelMessage(event, ticket, userId)
            TicketStatus.RESERVATION_CANCEL -> sendTicketCancel2Message(event, ticket, userId)
            else -> {}
        }
    }

    private fun sendTicketCancelMessage(
        event: UketEvent,
        ticket: Ticket,
        userId: Long,
    ) {
        val user = userService.getById(userId)
        val organization = organizationService.getById(event.organizationId)
        ticketCancelMessageSendService.send(
            user.name,
            user.phoneNumber!!,
            event.eventName,
            event.eventType,
            ticket.ticketNo,
            organization.name
        )
    }

    private fun sendTicketCancel2Message(
        event: UketEvent,
        ticket: Ticket,
        userId: Long,
    ) {
        val user = userService.getById(userId)
        ticketCancel2MessageSendService.send(
            user.name,
            event.eventType,
            event.eventName,
            ticket.ticketNo,
            user.phoneNumber!!,
        )
    }

    private fun reducePerformerTicketCount(
        ticket: Ticket,
        eventRound: UketEventRound,
    ) {
        if (ticket.performerName.isNotEmpty()) {
            val performer = performerService.findByNameAndRoundId(ticket.performerName, eventRound.id)
            performerService.minusTicketCountForPerformer(performer.id, 1)
        }
    }

    private fun checkTicketingTime(eventRound: UketEventRound, now: LocalDateTime) {
        check(eventRound.isCancelable(now)) {
            throw PublicException(
                publicMessage = "티켓 취소 가능한 시간이 아닙니다.",
                systemMessage = "[CancelTicketFacade] 티켓팅 기간이 아닐 때 티켓 취소 요청 | eventRoundId=${eventRound.id}"
            )
        }
    }
}
