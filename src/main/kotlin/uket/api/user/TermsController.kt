package uket.api.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uket.api.user.request.TermsAgreeRequest
import uket.api.user.response.TermsAgreeResponse
import uket.api.user.response.TermsResponse
import uket.auth.config.userId.LoginUserId
import uket.common.response.ListResponse
import uket.domain.terms.dto.TermsAgreeAnswer
import uket.domain.terms.entity.TermSign
import uket.facade.TermsDocumentFacade

@Tag(name = "약관 관련 API", description = "약관 관련 API 입니다")
@RestController
class TermsController(
    private val termsDocumentFacade: TermsDocumentFacade,
) {
    @Operation(summary = "유저 확인 필요 약관 목록 제공", description = "유저가 확인해야하는 약관 목록을 제공합니다")
    @GetMapping("/terms/check-required")
    fun getCheckRequiredTerms(
        @LoginUserId
        @Parameter(hidden = true)
        userId: Long,
    ): ResponseEntity<ListResponse<TermsResponse>> {
        val checkRequireTerms = termsDocumentFacade.getAllActiveAndCheckRequiredByUser(userId)
        val responses = checkRequireTerms.map { TermsResponse.of(it) }
        return ResponseEntity.ok(ListResponse(responses))
    }

    @Operation(summary = "유저 약관 동의 여부 제출", description = "유저의 약관 동의 여부를 제출합니다")
    @PostMapping("/terms/agreement")
    fun agreeTerms(
        @LoginUserId
        @Parameter(hidden = true)
        userId: Long,
        @RequestBody
        requests: List<TermsAgreeRequest>,
    ): ResponseEntity<ListResponse<TermsAgreeResponse>> {
        val userAnswers = requests
            .map { TermsAgreeAnswer(it.termsId, it.isAgree, it.documentId) }
        val termsSigns: List<TermSign> = termsDocumentFacade.agreeTerms(userId, userAnswers)
        val responses = termsSigns
            .map(TermsAgreeResponse::from)
        return ResponseEntity.ok(ListResponse(responses))
    }
}
