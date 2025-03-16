package uket.infra.output.persistence.entity.user

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jdk.jfr.Registered
import uket.infra.output.persistence.entity.BaseTimeEntity

@Entity
@AttributeOverride(name = "id", column = Column(name = "user_id"))
class UserEntity(
    platform: Platform?,
    platformId: Long,
    name: String,
    email: String,
    profileImage: String,
    depositorName: String,
    phoneNumber: String,
    isRegistered: Boolean
) : BaseTimeEntity() {
    var platform: Platform? = platform
        protected set

    var playformId: Long = platformId
        protected set

    var name: String = name
        protected set

    var email: String = email
        protected set

    var profileImage: String = profileImage
        protected set

    var depositorName: String = depositorName
        protected set

    var phoneNumber: String = phoneNumber
        protected set

    var isRegistered: Boolean = isRegistered
        protected set
}
