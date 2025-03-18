package uket.uket.domain.organization

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "organization")
class Organization(
    id: Long,
    name: String,
    organizationImagePath: String?
) : BaseTimeEntity() {

    @Id @GeneratedValue
    @Column(name = "organization_id")
    var id: Long = id

    @Column(nullable = false)
    var name: String = name
        protected set

    var organizationImagePath: String? = organizationImagePath
        protected set
}
