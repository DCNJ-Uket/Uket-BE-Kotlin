package uket.uket.modules.push.nhn

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding

@ConfigurationProperties(prefix = "app.message.nhn")
@ConfigurationPropertiesBinding
data class NHNMessageProperties(
    val appKey: String,
    val secretKey: String,
    val senderKey: String,
)
