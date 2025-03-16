package uket.infra.output.persistence.entity.payment

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "payment_history_id"))
class PaymentHistoryEntity(
    var organizationId: Long = 0L,
    var userId: Long = 0L,
    var price: Int = 0,
    var status: PaymentStatus? = null,
    var category: String = "",
    var manner: PaymentManner? = null,
    var description: String = ""
) : BaseTimeEntity() {
}
