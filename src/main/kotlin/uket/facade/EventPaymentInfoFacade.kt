package uket.uket.facade

import org.springframework.stereotype.Component
import uket.domain.payment.service.PaymentService
import uket.domain.uketevent.service.UketEventService
import uket.uket.facade.dto.PaymentWithTicketPriceResult

@Component
class EventPaymentInfoFacade(
    private val paymentService: PaymentService,
    private val uketEventService: UketEventService,
) {
    fun getEventPaymentInfoWithTicketPrice(uketEventId: Long): PaymentWithTicketPriceResult {
        val uketEvent = uketEventService.getById(uketEventId)
        val payment = paymentService.getById(uketEvent.paymentId)

        return PaymentWithTicketPriceResult(
            payment = payment,
            ticketPrice = uketEvent.ticketPrice
        )
    }
}
