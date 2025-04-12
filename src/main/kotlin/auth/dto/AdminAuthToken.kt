package uket.auth.dto

data class AdminAuthToken(
    val accessToken: String,
    val name: String,
    val email: String,
    val isSuperAdmin: Boolean,
) {
    companion object {
        fun of(accessToken: String, name: String,email: String, isSuperAdmin: Boolean): AdminAuthToken {
            return AdminAuthToken(accessToken, name,email, isSuperAdmin)
        }
    }
}
