package uket.uket.domain.payment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "payment_history")
class PaymentHistory(
    _id: Long,
    val organizationId: Long,
    val usersId: Long,
    val price: Int,
    val status: PaymentStatus,
    val category: String,
    val manner: PaymentManner,
    val description: String?,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "payment_history_id")
    val id: Long = _id
}
