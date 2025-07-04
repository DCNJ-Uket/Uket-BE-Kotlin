package uket.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.common.PublicException
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.uketevent.service.PerformerService
import uket.domain.uketevent.service.UketEventRoundService
import uket.domain.uketevent.service.UketEventService
import java.time.LocalDateTime

@Service
class CancelTicketFacade(
    val ticketService: TicketService,
    val entryGroupService: EntryGroupService,
    val uketEventRoundService: UketEventRoundService,
    val uketEventService: UketEventService,
    val performerService: PerformerService,
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

        val updatedTicket = changeTicketStatus(event, ticket)
        entryGroup.decreaseReservedCount();
        reducePerformerTicketCount(ticket, eventRound)

        return updatedTicket
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

    private fun changeTicketStatus(event: UketEvent, ticket: Ticket): Ticket {
        if (event.ticketPrice == 0L) {
            ticket.status = TicketStatus.RESERVATION_CANCEL
        } else {
            ticket.status = TicketStatus.REFUND_REQUESTED
        }
        return ticket;
    }

    private fun checkTicketingTime(eventRound: UketEventRound, now: LocalDateTime) {
        check(eventRound.isNowTicketing(now)) {
            throw PublicException(
                publicMessage = "티켓 취소 가능한 시간이 아닙니다.",
                systemMessage = "[CancelTicketFacade] 티켓팅 기간이 아닐 때 티켓 취소 요청 | eventRoundId=${eventRound.id}"
            )
        }
    }
}
