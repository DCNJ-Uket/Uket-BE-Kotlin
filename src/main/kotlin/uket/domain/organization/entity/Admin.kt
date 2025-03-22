package uket.uket.domain.organization.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "admin")
class Admin(
    _id: Long,
    _organization: Organization,
    val name: String,
    val email: String,
    var password: String?,
    val isSuperAdmin: Boolean,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "admin_id")
    val id: Long = _id

    @OneToOne
    @JoinColumn(name = "organization_id", nullable = false)
    val organization: Organization = _organization
}
