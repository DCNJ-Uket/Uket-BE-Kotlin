package uket.api.user.response

import uket.common.enums.BankCode
import uket.uket.facade.dto.PaymentWithTicketPriceResult

data class PaymentInfoResponse(
    val organizationId: Long,
    val depositLink: String,
    val ticketPrice: Int,
    val account: OrganizationAccount,
) {
    data class OrganizationAccount(
        val bankCode: BankCode,
        val accountNumber: String,
        val depositorName: String,
    )

    companion object {
        fun from(paymentWithTicketPrice: PaymentWithTicketPriceResult): PaymentInfoResponse {
            val payment = paymentWithTicketPrice.payment

            return PaymentInfoResponse(
                organizationId = payment.organizationId,
                depositLink = payment.depositLink,
                ticketPrice = paymentWithTicketPrice.ticketPrice,
                account = OrganizationAccount(
                    bankCode = payment.account.bankCode,
                    accountNumber = payment.account.accountNumber,
                    depositorName = payment.account.depositorName
                )
            )
        }
    }
}
