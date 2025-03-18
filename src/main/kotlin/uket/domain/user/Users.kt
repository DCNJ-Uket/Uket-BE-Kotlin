package uket.uket.domain.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "users")
class Users(
    id: Long,
    platform: Platform,
    platformId: Long,
    name: String,
    email: String,
    profileImage: String? = null,
    depositorName: String,
    phoneNumber: String,
    isRegistered: Boolean,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "users_id")
    var id: Long = id

    @Column(nullable = false)
    var platform: Platform = platform
        protected set

    @Column(nullable = false)
    var platformId: Long = platformId
        protected set

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var email: String = email
        protected set

    var profileImage: String? = profileImage
        protected set

    @Column(nullable = false)
    var depositorName: String = depositorName
        protected set

    @Column(nullable = false)
    var phoneNumber: String = phoneNumber
        protected set

    @Column(nullable = false)
    var isRegistered: Boolean = isRegistered
        protected set
}
