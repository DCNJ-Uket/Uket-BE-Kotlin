package uket.modules.email.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding

@ConfigurationProperties(prefix = "spring.mail")
@ConfigurationPropertiesBinding
data class EmailProperties(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val properties: EmailDetailProperties,
    val urls: UrlProperties,
)

data class EmailDetailProperties(
    val authCodeExpirationMillis: Long,
)

data class UrlProperties(
    val baseUrl: String,
    val imageUrl: String,
)
