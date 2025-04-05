package uket.uket.api.admin.request

data class EmailLoginRequest(
    val email: String,
    val password: String,
)
