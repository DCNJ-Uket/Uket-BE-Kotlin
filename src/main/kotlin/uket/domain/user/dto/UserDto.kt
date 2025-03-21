package uket.uket.domain.user.dto

import uket.uket.domain.user.entity.User
import uket.uket.domain.user.enums.Platform

@JvmRecord
data class UserDto(
    val userId: Long,
    val platform: Platform,
    val platformId: String,
    val name: String,
    val email: String,
    val profileImage: String,
    val depositorName: String? = null,
    val phoneNumber: String? = null,
    val isRegistered: Boolean = false,
) {
    companion object {
        fun from(
            user: User,
        ): UserDto = UserDto(
            user.id,
            user.platform,
            user.platformId,
            user.name,
            user.email,
            user.profileImage,
            user.depositorName,
            user.phoneNumber,
            user.isRegistered,
        )
    }
}
