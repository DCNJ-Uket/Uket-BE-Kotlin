package uket.infra.output.persistence.entity.payment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
class PaymentEntity(
    var organizationId: Long = 0L,
    var accountNo: String = "",
    var depositLink: String = ""
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private var id: Long = 0L

}
