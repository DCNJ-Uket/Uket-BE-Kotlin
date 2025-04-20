package uket.domain.user.enums

import java.security.InvalidParameterException

enum class Platform {
    KAKAO,
    GOOGLE,
    ;

    companion object {
        fun fromString(provider: String): Platform {
            val platform = provider.uppercase()
            if (platform == "KAKAO") {
                return KAKAO
            }
            if (platform == "GOOGLE") {
                return GOOGLE
            }
            throw InvalidParameterException()
        }
    }
}
