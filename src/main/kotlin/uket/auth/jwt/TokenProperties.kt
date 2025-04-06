package uket.auth.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties(prefix = "app.token")
@ConfigurationPropertiesBinding
data class TokenProperties(
    val secretKey: String,
    @NestedConfigurationProperty
    val expiration: TokenExpirationProperties,
) {
    @ConfigurationPropertiesBinding
    data class TokenExpirationProperties(
        val accessTokenExpiration: Long,
        val refreshTokenExpiration: Long,
        val emailTokenExpiration: Long,
    )
}
