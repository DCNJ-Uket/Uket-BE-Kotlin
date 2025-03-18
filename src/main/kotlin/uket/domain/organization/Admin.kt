package uket.uket.domain.organization

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "admin")
class Admin(
    id: Long,
    organization: Organization,
    name: String,
    email: String,
    password: String,
    isSuperAdmin: String
) : BaseTimeEntity() {

    @Id @GeneratedValue
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
