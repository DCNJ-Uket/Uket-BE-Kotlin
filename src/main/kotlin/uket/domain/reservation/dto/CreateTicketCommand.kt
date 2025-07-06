package uket.domain.reservation.dto

import uket.domain.reservation.enums.TicketStatus

data class CreateTicketCommand(
    val userId: Long,
    val entryGroupId: Long,
    val ticketStatus: TicketStatus,
    val performerName: String,
)
