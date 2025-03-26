package uket.uket.domain.payment.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.uket.domain.payment.entity.Payment
import uket.uket.domain.payment.repository.PaymentHistoryRepository

@Service
@Transactional(readOnly = true)
class PaymentHistoryService(
    val paymentHistoryRepository: PaymentHistoryRepository,
) {
    fun findById(paymentHIstoryId: Long): Payment {
        val paymentHistory = paymentHistoryRepository.findByIdOrNull(paymentHIstoryId)
            ?: throw IllegalStateException("해당 결제 기록을 찾을 수 없습니다")
        return paymentHistory
    }
}
