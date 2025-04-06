package uket.uket.modules.email.service

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MailSendService(
    private val javaMailSender: JavaMailSender,
) {
    fun sendEmail(to: String, subject: String, text: String) {
        val emailForm = createEmailForm(to, subject, text)
        try {
            javaMailSender.send(emailForm)
        } catch (e: RuntimeException) {
            throw RuntimeException("이메일 전송을 실패했습니다.")
        }
    }

    fun sendEmailWithHtmlContent(to: String, subject: String, htmlContent: String) {
        val message = createHtmlEmailForm(to, subject, htmlContent)
        try {
            javaMailSender.send(message)
        } catch (e: Exception) {
            throw RuntimeException("이메일 전송을 실패했습니다.")
        }
    }

    private fun createEmailForm(to: String, subject: String, text: String): SimpleMailMessage {
        return SimpleMailMessage().apply {
            setTo(to)
            setSubject(subject)
            setText(text)
        }
    }

    private fun createHtmlEmailForm(to: String, subject: String, htmlContent: String): MimeMessage {
        val message = javaMailSender.createMimeMessage()
        MimeMessageHelper(message, false, "UTF-8").apply {
            setTo(to)
            setSubject(subject)
            setText(htmlContent, true)
        }
        return message
    }
}
