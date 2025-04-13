package uket.auth.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding

@ConfigurationProperties(prefix = "app")
@ConfigurationPropertiesBinding
data class AppProperties(
    val kakao: KaKaoProperties,
    val google: GoogleProperties,
) {
    @ConfigurationPropertiesBinding
    data class KaKaoProperties(
        val tokenUri: String,
        val userInfoUri: String,
        val clientId: String,
        val clientSecret: String,
    )

    @ConfigurationPropertiesBinding
    data class GoogleProperties(
        val tokenUri: String,
        val userInfoUri: String,
        val clientId: String,
        val clientSecret: String,
    )
}
