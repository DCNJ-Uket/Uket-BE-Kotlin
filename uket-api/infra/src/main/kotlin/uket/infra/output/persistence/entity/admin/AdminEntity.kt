package uket.infra.output.persistence.entity.admin

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class AdminEntity(
    var organizationId: Long = 0L,
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var isSuperAdmin: String = "",
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @Id
    @GeneratedValue
    @Column(name = "admin_id")
    var id: Long = 0L
}
