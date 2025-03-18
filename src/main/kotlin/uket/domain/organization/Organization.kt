package uket.uket.domain.organization

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "organization")
class Organization(
    id: Long,
    name: String,
    organizationImagePath: String?,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "organization_id")
    var id: Long = id

    @Column(nullable = false)
    var name: String = name
        protected set

    var organizationImagePath: String? = organizationImagePath
        protected set
}
