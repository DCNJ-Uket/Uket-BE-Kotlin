package uket.api.admin.request

data class RegisterAdminPasswordCommand(
    val email: String,
    val password: String,
)
