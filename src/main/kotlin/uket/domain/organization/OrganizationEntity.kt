package uket.uket.domain.organization

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

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
