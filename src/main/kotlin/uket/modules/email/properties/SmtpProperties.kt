package uket.uket.modules.email.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding

@ConfigurationProperties(prefix = "spring.mail.properties.mail.smtp")
@ConfigurationPropertiesBinding
data class SmtpProperties(
    val auth: Boolean,
    val connectiontimeout: Long,
    val timeout: Long,
    val writetimeout: Long,
    val starttls: StarttlsProperties
)

data class StarttlsProperties(
    val enable: Boolean,
    val required: Boolean
)
