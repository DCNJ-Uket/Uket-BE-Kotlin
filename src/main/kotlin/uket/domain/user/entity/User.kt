package uket.domain.user.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import uket.domain.BaseTimeEntity
import uket.domain.user.enums.Platform

@Entity
@Table(
    name = "users",
    uniqueConstraints = [UniqueConstraint(columnNames = ["platform", "platform_id"])]
)
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
