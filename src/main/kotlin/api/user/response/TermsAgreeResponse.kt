package uket.api.user.response

import uket.domain.terms.entity.TermSign

data class TermsAgreeResponse(
    val termId: Long,
    val isAgreed: Boolean,
) {
    companion object {
        fun from(termSign: TermSign): TermsAgreeResponse = TermsAgreeResponse(
            termSign.termsId,
            termSign.isAgreed
        )
    }
}
