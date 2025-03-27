package uket.domain.organization.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import uket.domain.BaseTimeEntity

@Entity
@Table(name = "admin")
class Admin(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @OneToOne
    @JoinColumn(name = "organization_id", nullable = false)
    val organization: Organization,
    val name: String,
    val email: String,
    var password: String?,
    val isSuperAdmin: Boolean,
) : BaseTimeEntity()
