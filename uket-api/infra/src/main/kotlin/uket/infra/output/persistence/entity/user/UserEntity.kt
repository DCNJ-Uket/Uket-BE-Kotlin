package uket.infra.output.persistence.entity.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
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
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private var id: Long = 0L
}
