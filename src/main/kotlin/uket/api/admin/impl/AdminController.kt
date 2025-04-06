package uket.uket.api.admin.impl

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestController
import uket.domain.admin.dto.RegisterAdminWithoutPasswordCommand
import uket.uket.api.admin.AdminApi
import uket.uket.api.admin.request.EmailLoginRequest
import uket.uket.api.admin.request.RegisterAdminPasswordCommand
import uket.uket.api.admin.response.RegisterAdminResponse
import uket.uket.api.admin.response.SendEmailResponse
import uket.uket.auth.dto.AdminAuthToken
import uket.uket.facade.AdminAuthEmailFacade

@RestController
class AdminController(
    private val adminAuthEmailFacade: AdminAuthEmailFacade,
) : AdminApi {
    override fun sendInviteEmail(
        registerAdminWithoutPasswordCommand: RegisterAdminWithoutPasswordCommand,
    ): ResponseEntity<SendEmailResponse> {
        val response: SendEmailResponse = adminAuthEmailFacade.sendAuthEmail(registerAdminWithoutPasswordCommand)
        return ResponseEntity.ok(response)
    }

    override fun registerPassword(registerAdminPasswordCommand: RegisterAdminPasswordCommand): ResponseEntity<RegisterAdminResponse> {
        val authentication = SecurityContextHolder.getContext().authentication
        val token = authentication.credentials as String
        val response: RegisterAdminResponse = adminAuthEmailFacade.registerAdminWithPassword(token, registerAdminPasswordCommand)
        return ResponseEntity.ok(response)
    }

    override fun login(request: EmailLoginRequest): ResponseEntity<AdminAuthToken> {
        val adminAuthToken: AdminAuthToken = adminAuthEmailFacade.login(request.email, request.password)
        return ResponseEntity.ok(adminAuthToken)
    }
}
