package uket.uket.domain.payment.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.uket.domain.payment.entity.Payment

interface PaymentRepository : JpaRepository<Payment, Long>
