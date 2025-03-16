package uket.infra.output.persistence.entity.admin

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "admin_id"))
class AdminEntity(
    organizationId: Long,
    name: String,
    email: String,
    password: String,
    isSuperAdmin: String
) : BaseTimeEntity() {
    var organizationId: Long = organizationId
        protected set
    var name: String = name
        protected set
    var email: String = email
        protected set
    var password: String = password
        protected set
    var isSuperAdmin: String = isSuperAdmin
        protected set
}
