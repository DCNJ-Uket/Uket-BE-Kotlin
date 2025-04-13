package uket.api.admin.request

data class SendEmailRequest(
    val name: String,
    val email: String,
    val organization: String,
    val isSuperAdmin: Boolean,
)
