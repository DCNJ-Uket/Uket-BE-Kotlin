package uket.uket.domain.payment

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "payment")
@AttributeOverride(name = "id", column = Column(name = "payment_id"))
class PaymentEntity(
    organizationId: Long,
    accountNo: String,
    depositLink: String
) : BaseTimeEntity() {

    @Column(nullable = false)
    var organizationId: Long = organizationId
        protected set

    @Column(nullable = false)
    var accountNo: String = accountNo
        protected set

    @Column(nullable = false)
    var depositLink: String = depositLink
        protected set
}
