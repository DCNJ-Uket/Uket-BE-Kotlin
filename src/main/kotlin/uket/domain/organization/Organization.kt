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
    _id: Long,
    val name: String,
    val organizationImagePath: String?,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "organization_id")
    val id: Long = _id
}
