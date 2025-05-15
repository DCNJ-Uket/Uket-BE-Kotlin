package uket.domain.admin.dto

data class RegisterAdminWithoutPasswordCommand(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val organization: String,
    val isSuperAdmin: Boolean,
)
