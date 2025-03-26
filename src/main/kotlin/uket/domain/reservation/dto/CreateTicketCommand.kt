package uket.uket.domain.reservation.dto

import uket.uket.domain.reservation.enums.TicketStatus

data class CreateTicketCommand(
    val userId: Long,
    val entryGroupId: Long,
    val ticketStatus: TicketStatus,
)
