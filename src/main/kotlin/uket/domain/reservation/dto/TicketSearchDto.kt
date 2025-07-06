package uket.domain.reservation.dto

import uket.domain.reservation.enums.TicketStatus
import java.time.LocalDateTime

data class TicketSearchDto(
    val ticketId: Long,
    val depositorName: String,
    val telephone: String,
    val showTime: LocalDateTime,
    val orderDate: LocalDateTime,
    val updatedDate: LocalDateTime,
    val ticketStatus: TicketStatus,
    val performer: String,
)
