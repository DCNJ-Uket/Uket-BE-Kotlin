package uket.uket.auth.dto

data class AdminAuthToken(
    val accessToken: String,
    val name: String
) {
    companion object {
        fun from(accessToken: String, name: String): AdminAuthToken {
            return AdminAuthToken(accessToken, name)
        }
    }
}
