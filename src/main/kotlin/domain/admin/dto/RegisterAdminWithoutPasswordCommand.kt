package uket.domain.admin.dto

data class RegisterAdminWithoutPasswordCommand(
    val name: String,
    val email: String,
    val organization: String,
    val authority: String,
)
