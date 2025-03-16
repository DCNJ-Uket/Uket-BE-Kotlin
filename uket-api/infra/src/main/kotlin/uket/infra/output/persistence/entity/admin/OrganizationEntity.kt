package uket.infra.output.persistence.entity.admin

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "organization")
@AttributeOverride(name = "id", column = Column(name = "organization_id"))
class OrganizationEntity(
    name: String,
    organizationImagePath: String?
) : BaseTimeEntity() {

    @Column(nullable = false)
    var name: String = name
        protected set

    var organizationImagePath: String? = organizationImagePath
        protected set
}
