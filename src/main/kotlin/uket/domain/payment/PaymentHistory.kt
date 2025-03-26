package uket.uket.domain.payment

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "payment_history")
class PaymentHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val organizationId: Long,
    val usersId: Long,
    val price: Int,
    @Enumerated(EnumType.STRING)
    val status: PaymentStatus,
    val category: String,
    @Enumerated(EnumType.STRING)
    val manner: PaymentManner,
    val description: String?,
) : BaseTimeEntity()
