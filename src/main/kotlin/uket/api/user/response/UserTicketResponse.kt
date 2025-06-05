package uket.api.user.response

import uket.domain.reservation.entity.Ticket
import java.time.LocalDateTime

data class UserTicketResponse(
    val ticketId: Long,
    val userId: Long,
    val entryGroupId: Long,
    var ticketStatus: String,
    val ticketNo: String,
    var enterAt: LocalDateTime?,
) {
    companion object {
        fun of(ticket: Ticket): UserTicketResponse = UserTicketResponse(
            ticketId = ticket.id,
            userId = ticket.userId,
            entryGroupId = ticket.entryGroupId,
            ticketStatus = ticket.status.value,
            ticketNo = ticket.ticketNo,
            enterAt = ticket.enterAt
        )
    }
}
