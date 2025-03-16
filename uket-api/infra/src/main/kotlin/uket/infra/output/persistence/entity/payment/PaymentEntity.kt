package uket.infra.output.persistence.entity.payment

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
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
