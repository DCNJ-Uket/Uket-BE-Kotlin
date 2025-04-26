package uket.auth.dto.response.userinfo

interface OAuth2UserInfoResponse {
    val provider: String

    val providerId: String

    val email: String

    val name: String

    val profileImage: String
}
