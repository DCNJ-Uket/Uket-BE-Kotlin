package uket.modules.email.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import uket.modules.email.properties.EmailProperties
import uket.modules.email.properties.SmtpProperties
import java.util.Properties

@Configuration
class EmailConfig(
    private val emailProperties: EmailProperties,
    private val smtpProperties: SmtpProperties,
) {
    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl().apply {
            host = emailProperties.host
            port = emailProperties.port
            username = emailProperties.username
            password = emailProperties.password
            defaultEncoding = "UTF-8"
            javaMailProperties = getMailProperties()
        }
        return mailSender
    }

    private fun getMailProperties(): Properties {
        return Properties().apply {
            put("mail.smtp.auth", smtpProperties.auth)
            put("mail.smtp.starttls.enable", smtpProperties.starttls.enable)
            put("mail.smtp.starttls.required", smtpProperties.starttls.required)
            put("mail.smtp.connectiontimeout", smtpProperties.connectiontimeout)
            put("mail.smtp.timeout", smtpProperties.timeout)
            put("mail.smtp.writetimeout", smtpProperties.writetimeout)
        }
    }
}
