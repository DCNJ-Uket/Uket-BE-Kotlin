package uket.infra.output.persistence.entity.payment

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "payment_id"))
class PaymentEntity(
    var organizationId: Long = 0L,
    var accountNo: String = "",
    var depositLink: String = ""
) : BaseTimeEntity() {
}
