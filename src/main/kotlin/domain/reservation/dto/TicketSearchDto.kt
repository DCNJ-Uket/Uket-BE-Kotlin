package uket.domain.reservation.dto

import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.user.entity.User
import java.time.LocalDateTime

data class TicketSearchDto(
    val ticketId: Long,
    val depositorName: String,
    val telephone: String,
    val showTime: LocalDateTime,
    val orderDate: LocalDateTime,
    val updatedDate: LocalDateTime,
    val ticketStatus: TicketStatus,
    val friend: String,
) {
    companion object {
        fun from(ticket: Ticket, user: User, uketEventRound: UketEventRound): TicketSearchDto {
            return TicketSearchDto(
                ticketId = ticket.id,
                depositorName = user.depositorName!!,
                telephone = user.phoneNumber!!,
                showTime = uketEventRound.eventRoundDateTime,
                orderDate = ticket.createdAt,
                updatedDate = ticket.updatedAt,
                ticketStatus = ticket.status,
                friend = ""
            )
        }
    }
}
