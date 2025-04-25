package uket.api.admin.response

import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus

data class UpdateTicketStatusResponse(
    val ticketId: Long,
    val status: TicketStatus,
) {
    companion object {
        fun from(ticket: Ticket): UpdateTicketStatusResponse {
            return UpdateTicketStatusResponse(
                ticketId = ticket.id,
                status = ticket.status,
            )
        }
    }
}
