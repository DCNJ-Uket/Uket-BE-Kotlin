package uket.infra.output.persistence.entity.admin

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "admin_id"))
class AdminEntity(
    var organizationId: Long = 0L,
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var isSuperAdmin: String = ""
) : BaseTimeEntity() {
}
