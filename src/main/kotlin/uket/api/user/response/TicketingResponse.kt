package uket.api.user.response

import uket.common.enums.BankCode

data class TicketingResponse(
    val ticketIds: List<Long>,
    val totalPrice: Long,
    val depositUrl: String,
    val bankCode: BankCode,
)
