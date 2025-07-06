package uket.api.user.response

import uket.common.enums.BankCode
import uket.domain.payment.entity.Payment

data class TicketingResponse(
    val ticketIds: List<Long>,
    val totalPrice: Long,
    val account: Payment.Account,
    val depositUrl: String,
    val bankCode: BankCode,
)
