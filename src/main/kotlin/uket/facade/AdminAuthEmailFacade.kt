package uket.uket.facade

import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.auth.jwt.JwtAuthTokenUtil
import uket.domain.admin.dto.RegisterAdminWithoutPasswordCommand
import uket.domain.admin.entity.Admin
import uket.domain.admin.entity.Organization
import uket.domain.admin.service.AdminService
import uket.domain.admin.service.OrganizationService
import uket.uket.api.admin.request.RegisterAdminPasswordCommand
import uket.uket.api.admin.response.RegisterAdminResponse
import uket.uket.api.admin.response.SendEmailResponse
import uket.uket.auth.dto.AdminAuthToken
import uket.uket.domain.user.enums.UserRole
import uket.uket.modules.email.properties.EmailProperties
import uket.uket.modules.email.service.MailSendService
import uket.uket.modules.redis.util.RedisUtil

@Service
@Transactional
class AdminAuthEmailFacade(
    private val mailService: MailSendService,
    private val adminService: AdminService,
    private val emailProperties: EmailProperties,
    private val redisUtil: RedisUtil,
    private val jwtAuthTokenUtil: JwtAuthTokenUtil,
    private val organizationService: OrganizationService
) {

    companion object {
        private const val EMAIL_TOKEN_PREFIX = "EMAIL_TOKEN:"
        private const val EMAIL_TOKEN_EXPIRATION_MILLIS = 1000 * 60 * 60 * 24 // 24시간
    }

    fun sendAuthEmail(command: RegisterAdminWithoutPasswordCommand):SendEmailResponse {
        validateEmail(command.email)
        val organization: Organization = organizationService.findByName(command.organization)
        val admin = adminService.registerAdminWithoutPassword(command, organization)

        val token = jwtAuthTokenUtil.createEmailToken(
            admin.id, admin.email, admin.name, true
        )
        redisUtil.setDataExpire(EMAIL_TOKEN_PREFIX+token, admin.email, EMAIL_TOKEN_EXPIRATION_MILLIS.toLong())

        val subject = "[Uket admin] 회원가입 링크 안내"
        val content = loadAdminInviteHtml(token)

        mailService.sendEmailWithHtmlContent(admin.email, subject, content)
        return SendEmailResponse.from(admin)
    }

    fun registerAdminWithPassword(token: String, command: RegisterAdminPasswordCommand):RegisterAdminResponse {
        validateEmailInRedis(token, command.email)
        val admin: Admin = adminService.updatePassword(command.email,command.password)
        return RegisterAdminResponse.of(admin, admin.organization)
    }

    fun login(email: String, password: String): AdminAuthToken {
        val admin = adminService.findByEmail(email)

        validateRegistered(admin)
        val accessToken = jwtAuthTokenUtil.createAccessToken(
            admin.id, admin.name,
            java.lang.String.valueOf(UserRole.ADMIN), true
        )
        return AdminAuthToken.from(accessToken, admin.name)
    }

    private fun validateEmail(email: String) {
        adminService.checkDuplicateEmail(email)
    }

    private fun validateEmailInRedis(token: String, requestEmail: String) {
        val redisKey = EMAIL_TOKEN_PREFIX + token
        System.out.println(redisKey)
        val savedEmail = redisUtil.getData(redisKey).orElseThrow {
            throw IllegalStateException("이메일 인증 토큰이 만료되었거나 유효하지 않습니다.")
        }

        if (savedEmail != requestEmail) {
            throw IllegalStateException("요청 이메일과 인증된 이메일이 일치하지 않습니다.")
        }
    }

    private fun loadAdminInviteHtml(token: String): String {
        val template = ClassPathResource("static/invite.html")
            .inputStream.bufferedReader().use { it.readText() }

        return template
            .replace("{{IMAGE_URL}}", emailProperties.urls.imageUrl)
            .replace("{{SIGNUP_LINK}}", "${emailProperties.urls.baseUrl}?token=$token")
    }

    private fun validateRegistered(admin: Admin) {
        if (admin.password == null) {
            throw IllegalStateException("회원가입 후 이용해주세요")
        }
    }
}
