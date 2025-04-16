package uket.api.user.request

data class LoginRequest(
//    @Schema(
//        description = "인가 코드",
//        example = "C95mSFwbWNVWUA1OCQ9mLSYFU5Dj7b5sFTgsy5lUNAAABjwtYyp"
//    )
//    @NotNull(message = "code 는 null 일 수 없습니다.")
    val code: String,
//    @Schema(
//        description = "Redirect URI",
//        example = "http://localhost:8080/login/oauth2/code/kakao"
//    )
//    @NotNull(message = "redirectUri 은 null 일 수 없습니다.")
    val redirectUri: String,
)
