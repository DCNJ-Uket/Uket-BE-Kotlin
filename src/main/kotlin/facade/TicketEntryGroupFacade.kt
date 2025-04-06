package uket.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.uketevent.service.EntryGroupService

@Service
@Transactional(readOnly = true)
class TicketEntryGroupFacade(
    val ticketService: TicketService,
    val entryGroupService: EntryGroupService,
) {
//    @Transactional
//    fun deleteAllUserTickets(userId: Long) {
//        val tickets: List<Ticket> = ticketService.findAllByUserIdAndStatusNotWithEntryGroup(userId, TicketStatus.RESERVATION_CANCEL)
//        for (ticket in tickets) {
//            entryGroupService.decreaseReservedCount(ticket.entryGroupId)
//        }
//        ticketService.deleteAllByUserId(userId)
//    }

    @Transactional
    fun updateTicketStatus(ticketId: Long, ticketStatus: TicketStatus) {
        val ticket = ticketService.getById(ticketId)

        if (ticketStatus === TicketStatus.RESERVATION_CANCEL) {
            entryGroupService.decreaseReservedCount(ticket.entryGroupId)
        }

        if (ticketStatus === TicketStatus.FINISH_ENTER) {
            ticket.enter()
            ticketService.updateTicket(ticket)
            return
        }

        val updatedTicket = ticket.updateStatus(ticketStatus)
        ticketService.updateTicket(updatedTicket)
    }
}
