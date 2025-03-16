package uket.infra.output.persistence.entity.payment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class PaymentEntity(
    var organizationId: Long = 0L,
    var accountNo: String = "",
    var depositLink: String = "",
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private var id: Long = 0L

}
