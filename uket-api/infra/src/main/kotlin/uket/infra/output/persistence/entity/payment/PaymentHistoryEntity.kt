package uket.infra.output.persistence.entity.payment

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "payment_history_id"))
class PaymentHistoryEntity(
    organizationId: Long,
    userId: Long,
    price: Int,
    status: PaymentStatus?,
    category: String,
    manner: PaymentManner?,
    description: String
) : BaseTimeEntity() {
    var organizationId: Long = organizationId
        protected set
    var userId: Long = userId
        protected set
    var price: Int = price
        protected set
    var status: PaymentStatus? = status
        protected set
    var category: String = category
        protected set
    var manner: PaymentManner? = manner
        protected set
    var description: String = description
        protected set
}
