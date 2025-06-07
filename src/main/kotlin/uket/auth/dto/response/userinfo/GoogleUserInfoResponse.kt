package uket.auth.dto.response.userinfo

class GoogleUserInfoResponse(
    private val attribute: Map<String, Any>,
    override val provider: String = "google",
    override val providerId: String = attribute["id"].toString(),
    override val email: String = attribute["email"].toString(),
    override val name: String = attribute["name"].toString(),
    override val profileImage: String = attribute["picture"].toString(),
) : OAuth2UserInfoResponse
