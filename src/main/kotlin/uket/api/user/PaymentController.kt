package uket.api.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import uket.api.user.response.PaymentInfoResponse
import uket.uket.facade.EventPaymentInfoFacade

@RestController
class PaymentController(
    private val eventPaymentInfoFacade: EventPaymentInfoFacade,
) {
    @GetMapping("/api/payments/uket-events/{uketEventId}")
    fun getOrganizationsPaymentInfo(
        @PathVariable("uketEventId") uketEventId: Long,
    ): PaymentInfoResponse {
        val paymentWithTicketPrice = eventPaymentInfoFacade.getEventPaymentInfoWithTicketPrice(uketEventId)
        return PaymentInfoResponse.from(paymentWithTicketPrice)
    }
}
