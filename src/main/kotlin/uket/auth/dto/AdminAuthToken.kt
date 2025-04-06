package uket.uket.auth.dto

data class AdminAuthToken(
    val accessToken: String,
    val name: String,
    val authority: String,
) {
    companion object {
        fun from(accessToken: String, name: String, authority: String): AdminAuthToken {
            return AdminAuthToken(accessToken, name, authority)
        }
    }
}
