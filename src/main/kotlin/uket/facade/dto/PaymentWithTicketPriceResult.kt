package uket.uket.facade.dto

import uket.domain.payment.entity.Payment

data class PaymentWithTicketPriceResult(
    val payment: Payment,
    val ticketPrice: Long,
)
