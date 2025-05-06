package uket.api.admin.request

data class RegisterAdminPasswordRequest(
    val email: String,
    val password: String,
    val phoneNumber: String,
)
