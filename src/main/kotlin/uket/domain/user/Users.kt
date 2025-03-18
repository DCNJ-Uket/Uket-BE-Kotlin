package uket.uket.domain.user

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "users")
@AttributeOverride(name = "id", column = Column(name = "users_id"))
class Users(
    platform: Platform,
    platformId: Long,
    name: String,
    email: String,
    profileImage: String? = null,
    depositorName: String,
    phoneNumber: String,
    isRegistered: Boolean
) : BaseTimeEntity() {

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
