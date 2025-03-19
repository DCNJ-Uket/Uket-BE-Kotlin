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
    val platform: Platform,
    val platformId: Long,
    val name: String,
    val email: String,
    val profileImage: String? = null,
    val depositorName: String,
    val phoneNumber: String,
    val isRegistered: Boolean,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "users_id")
    val id: Long = id
}
