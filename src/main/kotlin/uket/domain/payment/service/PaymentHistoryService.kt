package uket.domain.payment.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.payment.entity.Payment
import uket.domain.payment.repository.PaymentHistoryRepository

@Service
@Transactional(readOnly = true)
class PaymentHistoryService(
    private val paymentHistoryRepository: PaymentHistoryRepository,
) {
    fun getById(paymentHIstoryId: Long): Payment {
        val paymentHistory = paymentHistoryRepository.findByIdOrNull(paymentHIstoryId)
            ?: throw IllegalStateException("해당 결제 기록을 찾을 수 없습니다")
        return paymentHistory
    }
}
