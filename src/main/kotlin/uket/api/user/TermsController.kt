package uket.api.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uket.api.user.request.TermsAgreeRequest
import uket.api.user.response.TermsAgreeResponse
import uket.api.user.response.TermsResponse
import uket.auth.config.adminId.LoginUserId
import uket.common.response.ListResponse
import uket.domain.terms.service.TermsService

@Tag(name = "약관 관련 API", description = "약관 관련 API 입니다")
@RestController
class TermsController(
    private val termsService: TermsService,
) {
    @Operation(summary = "유저 미확인 약관 목록 제공", description = "유저가 확인해야하는 약관 목록을 제공합니다")
    @GetMapping("/terms/not-checked")
    fun getCheckRequiredTerms(
        @LoginUserId
        @Parameter(hidden = true)
        userId: Long,
    ): ResponseEntity<ListResponse<TermsResponse>> {
        val checkRequireTerms = termsService.getAllActiveAndCheckRequiredByUser(userId)
        val responses = checkRequireTerms.map { TermsResponse.of(it) }
        return ResponseEntity.ok(ListResponse(responses))
    }

    fun agreeTerms(
        @LoginUserId
        @Parameter(hidden = true)
        userId: Long,
        @RequestBody
        requests: List<TermsAgreeRequest>,
    ): ResponseEntity<ListResponse<TermsAgreeResponse>> {
        //
        return ResponseEntity.ok(ListResponse(listOf()))
    }
}
