package uket.auth.dto.response.userinfo

class KakaoUserInfoResponse(
    private val attribute: Map<String, Any>,
    private val account: Map<String, Any> = attribute["kakao_account"] as Map<String, Any>,
    private val profile: Map<String, Any> = account["profile"] as Map<String, Any>,
    override val provider: String = "kakao",
    override val providerId: String = attribute["id"].toString(),
    override val email: String = account["email"].toString(),
    override val name: String = profile["nickname"].toString(),
    override val profileImage: String = profile["thumbnail_image_url"].toString(),
) : OAuth2UserInfoResponse
