package uket.domain.payment.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.domain.payment.entity.Payment

interface PaymentRepository : JpaRepository<Payment, Long> {
    fun findByUketEventId(uketEventId: Long): Payment?
}
