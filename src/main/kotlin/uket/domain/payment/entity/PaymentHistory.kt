package uket.uket.domain.payment.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity
import uket.uket.domain.payment.enums.PaymentManner
import uket.uket.domain.payment.enums.PaymentStatus

@Entity
@Table(name = "payment_history")
class PaymentHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val organizationId: Long,
    val userId: Long,
    val price: Int,
    @Enumerated(EnumType.STRING)
    val status: PaymentStatus,
    val category: String,
    @Enumerated(EnumType.STRING)
    val manner: PaymentManner,
    val description: String?,
) : BaseTimeEntity()
