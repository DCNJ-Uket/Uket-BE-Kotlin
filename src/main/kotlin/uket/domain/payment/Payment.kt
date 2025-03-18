package uket.uket.domain.payment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "payment")
class Payment(
    _id: Long,
    val organizationId: Long,
    val accountNo: String,
    val depositLink: String,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    val id: Long = _id
}
