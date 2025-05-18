package uket.api.user.response

import uket.domain.user.entity.User

data class UserInfoResponse(
    val name: String,
    val email: String,
    val isRegistered: Boolean,
    val depositorName: String,
    val profileImagePath: String,
    val phoneNumber: String,
) {
    companion object {
        fun from(user: User): UserInfoResponse = UserInfoResponse(
            name = user.name,
            email = user.email,
            isRegistered = user.isRegistered,
            depositorName = user.depositorName ?: "",
            profileImagePath = user.profileImage,
            phoneNumber = user.phoneNumber ?: ""
        )
    }
}
