package uket.uket.domain.payment

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "payment_history")
@AttributeOverride(name = "id", column = Column(name = "payment_history_id"))
class PaymentHistory(
    organizationId: Long,
    usersId: Long,
    price: Int,
    status: PaymentStatus,
    category: String,
    manner: PaymentManner,
    description: String?
) : BaseTimeEntity() {

    @Column(nullable = false)
    var organizationId: Long = organizationId
        protected set

    @Column(nullable = false)
    var userId: Long = usersId
        protected set

    @Column(nullable = false)
    var price: Int = price
        protected set

    @Column(nullable = false)
    var status: PaymentStatus = status
        protected set

    @Column(nullable = false)
    var category: String = category
        protected set

    @Column(nullable = false)
    var manner: PaymentManner = manner
        protected set

    var description: String? = description
        protected set
}
