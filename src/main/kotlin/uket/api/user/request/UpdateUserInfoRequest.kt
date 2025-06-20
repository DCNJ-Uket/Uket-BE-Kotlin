package uket.api.user.request

data class UpdateUserInfoRequest(
    val email: String? = null,
    val name: String? = null,
    val profileImage: String? = null,
    val depositorName: String? = null,
    val phoneNumber: String? = null,
)
