package uket.uket.domain.payment.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.uket.domain.payment.entity.Payment
import uket.uket.domain.payment.repository.PaymentRepository

@Service
@Transactional(readOnly = true)
class PaymentService(val paymentRepository: PaymentRepository) {

    fun findById(paymentId: Long): Payment {
        val payment = paymentRepository.findByIdOrNull(paymentId)
            ?: throw IllegalStateException("해당 결제 정보를 찾을 수 없습니다")
        return payment
    }

}
