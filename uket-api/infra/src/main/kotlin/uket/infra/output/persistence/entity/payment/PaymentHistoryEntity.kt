package uket.infra.output.persistence.entity.payment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
class PaymentHistoryEntity(
    var organizationId: Long = 0L,
    var userId: Long = 0L,
    var price: Int = 0,
    var status: PaymentStatus? = null,
    var category: String = "",
    var manner: PaymentManner? = null,
    var description: String = ""
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "payment_history_id")
    private var id: Long = 0L

}
