package uket.infra.output.persistence.entity.user

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Entity
import uket.infra.output.persistence.entity.BaseTimeEntity

@Entity
@AttributeOverride(name = "id", column = Column(name = "user_id"))
class UserEntity(
    var platform: Platform? = null,
    var playformId: Long = 0L,
    var name: String = "",
    var email: String = "",
    var profileImage: String = "",
    var depositorName: String = "",
    var phoneNumber: String = "",
    var isRegistered: Boolean = false
) : BaseTimeEntity() {
}
