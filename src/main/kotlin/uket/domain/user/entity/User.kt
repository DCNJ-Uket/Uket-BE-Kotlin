package uket.uket.domain.user.entity

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity
import uket.uket.domain.user.enums.Platform

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Enumerated(EnumType.STRING)
    val platform: Platform,
    val platformId: String,
    var name: String,
    var email: String,
    var profileImage: String,
    var depositorName: String? = null,
    var phoneNumber: String? = null,
    var isRegistered: Boolean = false,
) : BaseTimeEntity() {
    fun updateProfile(email: String, name: String, profileImage: String) {
        this.email = email
        this.name = name
        this.profileImage = profileImage
    }

    fun register(depositorName: String, phoneNumber: String) {
        this.isRegistered = true
        this.depositorName = depositorName
        this.phoneNumber = phoneNumber
    }
}
