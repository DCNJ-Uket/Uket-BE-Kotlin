package uket.uket.domain.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity
import uket.uket.domain.user.enums.Platform

@Entity
@Table(name = "users")
class User(
    _id: Long,
    val platform: Platform,
    val platformId: String,
    var name: String,
    var email: String,
    var profileImage: String,
    var depositorName: String? = null,
    var phoneNumber: String? = null,
    var isRegistered: Boolean = false,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    val id: Long = _id

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
