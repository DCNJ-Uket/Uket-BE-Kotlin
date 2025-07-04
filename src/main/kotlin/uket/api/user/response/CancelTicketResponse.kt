package uket.api.user.response

import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus

data class CancelTicketResponse(
    val ticketId: Long,
    val ticketStatus: TicketStatus,
) {
    companion object {
        fun from(ticket: Ticket): CancelTicketResponse = CancelTicketResponse(
            ticket.id,
            ticket.status
        )
    }
}
