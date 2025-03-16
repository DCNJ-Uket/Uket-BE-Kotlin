package uket.infra.output.persistence.entity.admin

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "organization_id"))
class OrganizationEntity(
    var name: String = "",
    var organizationImagePath: String= ""
) : BaseTimeEntity() {
}
