package uket.api.admin.dto

import uket.domain.reservation.enums.TicketStatus
import java.time.LocalDateTime

data class LiveEnterUserDto(
    val enterTime: LocalDateTime,
    val name: String,
    val ticketDate: LocalDateTime,
    val phoneNumber: String,
    val ticketStatus: TicketStatus,
)
