package uket.infra.output.persistence.entity.admin

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "admin")
@AttributeOverride(name = "id", column = Column(name = "admin_id"))
class AdminEntity(
    organizationEntity: OrganizationEntity,
    name: String,
    email: String,
    password: String,
    isSuperAdmin: String
) : BaseTimeEntity() {

    @OneToOne
    @JoinColumn(name = "organization_id", nullable = false)
    var organizationEntity: OrganizationEntity = organizationEntity
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
