package uket.facade

import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.api.admin.request.SendEmailRequest
import uket.api.admin.response.RegisterAdminResponse
import uket.api.admin.response.SendEmailResponse
import uket.auth.dto.AdminAuthToken
import uket.auth.jwt.JwtAuthTokenUtil
import uket.common.PublicException
import uket.domain.admin.entity.Admin
import uket.domain.admin.entity.Organization
import uket.domain.admin.service.AdminService
import uket.domain.admin.service.OrganizationService
import uket.domain.user.enums.UserRole
import uket.modules.email.properties.EmailProperties
import uket.modules.email.service.MailSendService
import uket.modules.redis.util.RedisUtil

@Service
class AdminAuthEmailFacade(
    private val mailService: MailSendService,
    private val adminService: AdminService,
    private val emailProperties: EmailProperties,
    private val redisUtil: RedisUtil,
    private val jwtAuthTokenUtil: JwtAuthTokenUtil,
    private val organizationService: OrganizationService,
) {
    companion object {
        private const val EMAIL_TOKEN_PREFIX = "EMAIL_TOKEN:"
        private const val EMAIL_TOKEN_EXPIRATION_MILLIS = 1000 * 60 * 60 * 24 // 24시간
    }

    fun sendAuthEmail(request: SendEmailRequest): SendEmailResponse {
        validateEmail(request.email)
        validateOrganization(request.organization)
        val organization: Organization = organizationService.getByName(request.organization)
        val admin = adminService.registerAdminWithoutPassword(
            name = request.name,
            email = request.email,
            phoneNumber = request.phoneNumber,
            isSuperAdmin = request.isSuperAdmin,
            organization = organization
        )

        val token = jwtAuthTokenUtil.createEmailToken(
            admin.id,
            admin.email,
            admin.name,
            true,
        )
        redisUtil.setDataExpire(EMAIL_TOKEN_PREFIX + token, admin.email, EMAIL_TOKEN_EXPIRATION_MILLIS.toLong())

        val subject = "[Uket admin] 회원가입 링크 안내"
        val content = loadAdminInviteHtml(admin.email, token)

        mailService.sendEmailWithHtmlContent(admin.email, subject, content)
        return SendEmailResponse.from(admin)
    }

    @Transactional
    fun registerAdminWithPassword(token: String, email: String, password: String): RegisterAdminResponse {
        validateEmailInRedis(token, email)
        val admin: Admin = adminService.updatePassword(email, password)
        return RegisterAdminResponse.of(admin, admin.organization)
    }

    @Transactional(readOnly = true)
    fun login(email: String, password: String): AdminAuthToken {
        val admin = adminService.getByEmail(email)
        checkNotNull(admin.password) { "초대 메일을 통해 회원가입 후 이용 바랍니다." }

        validateRegistered(admin)
        validatePassword(email, password)
        val accessToken = jwtAuthTokenUtil.createAccessToken(
            admin.id,
            admin.name,
            UserRole.ADMIN.toString(),
            true,
        )
        return AdminAuthToken.of(accessToken, admin, admin.organization.name)
    }

    fun checkEmailTokenExpired(token: String): Boolean {
        val redisKey = EMAIL_TOKEN_PREFIX + token
        val key = redisUtil.getData(redisKey).orElse(null)
        return key == null
    }

    private fun validateEmail(email: String) {
        adminService.checkDuplicateEmail(email)
    }

    private fun validatePassword(email: String, password: String) {
        adminService.checkPassword(email, password)
    }

    private fun validateOrganization(organizationName: String) {
        organizationService.checkDuplicateOrganizationRegister(organizationName)
    }

    private fun validateEmailInRedis(token: String, requestEmail: String) {
        val redisKey = EMAIL_TOKEN_PREFIX + token
        val savedEmail = redisUtil.getData(redisKey).orElseThrow {
            throw PublicException(
                publicMessage = "이메일 인증 토큰이 만료되었거나 유효하지 않습니다.",
                systemMessage = "Not Registered Token : TOKEN =$token",
                title = "토큰 불일치"
            )
        }

        check(savedEmail == requestEmail) {
            throw PublicException(
                publicMessage = "요청 이메일과 인증된 이메일이 일치하지 않습니다.",
                systemMessage = "Invalid Request email : REQUESTEMAIL =$requestEmail",
                title = "잘못된 이메일 요청"
            )
        }
    }

    private fun loadAdminInviteHtml(email: String, token: String): String {
        val template = ClassPathResource("static/invite.html")
            .inputStream
            .bufferedReader()
            .use { it.readText() }

        return template
            .replace("{{IMAGE_URL}}", emailProperties.urls.imageUrl)
            .replace("{{SIGNUP_LINK}}", "${emailProperties.urls.baseUrl}?email=$email&token=$token")
    }

    private fun validateRegistered(admin: Admin) {
        checkNotNull(admin.password) {
            throw PublicException(
                publicMessage = "회원가입 후 이용해주세요.",
                systemMessage = "Not Registered Admin : ADMINID =${admin.id}",
                title = "회원 미가입"
            )
        }
    }
}
