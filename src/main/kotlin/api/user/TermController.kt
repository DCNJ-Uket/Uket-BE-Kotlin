package uket.api.user

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uket.api.user.request.TermsAgreeRequest
import uket.api.user.response.TermsAgreeResponse
import uket.api.user.response.TermsResponse
import uket.auth.config.adminId.LoginUserId
import uket.common.response.ListResponse

@Tag(name = "약관 관련 API", description = "약관 관련 API 입니다")
@RestController
class TermController {
    fun getTerms(
        @LoginUserId
        @Parameter(hidden = true)
        userId: Long,
    ): ResponseEntity<ListResponse<TermsResponse>> {
        //
        return ResponseEntity.ok(ListResponse(listOf()))
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
