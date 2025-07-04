package uket.domain.payment.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.common.PublicNotFoundException
import uket.domain.payment.entity.Payment
import uket.domain.payment.repository.PaymentRepository

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
) {
    @Transactional(readOnly = true)
    fun getById(paymentId: Long): Payment {
        val payment = paymentRepository.findByIdOrNull(paymentId)
            ?: throw PublicNotFoundException(
                publicMessage = "결제 정보를 찾을 수 없습니다",
                systemMessage = "[PaymentService] 해당 결제정보를 찾을 수 없습니다. | paymentId: $paymentId"
            )
        return payment
    }

    @Transactional(readOnly = true)
    fun getByOrganizationId(organizationId: Long): List<Payment> {
        val payment = paymentRepository.findByOrganizationId(organizationId)
        if (payment.isEmpty()) {
            throw PublicNotFoundException(
                publicMessage = "결제 정보를 찾을 수 없습니다",
                systemMessage = "[PaymentService] Organization 의 결제정보를 찾을 수 없습니다. | organizationId: $organizationId"
            )
        }
        return payment
    }

    @Transactional
    fun save(payment: Payment): Payment = paymentRepository.save(payment)
}
