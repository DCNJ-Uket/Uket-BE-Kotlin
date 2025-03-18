package uket.uket.domain.organization

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
    id: Long,
    organization: Organization,
    name: String,
    email: String,
    password: String,
    isSuperAdmin: String,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "admin_id")
    var id: Long = id

    @OneToOne
    @JoinColumn(name = "organization_id", nullable = false)
    var organization: Organization = organization
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
