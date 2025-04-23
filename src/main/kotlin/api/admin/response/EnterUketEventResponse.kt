package uket.api.admin.response

import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.user.entity.User

data class EnterUketEventResponse(
    val ticketId: Long,
    val userId: Long,
    val userName: String,
    val status: TicketStatus,
    val msg: String,
) {
    companion object {
        fun of(ticket: Ticket, user: User): EnterUketEventResponse {
            return EnterUketEventResponse(
                ticketId = ticket.id,
                userId =  ticket.userId,
                userName = user.name,
                status = ticket.status,
                msg = ticket.status.msg,
            )
        }
    }
}
