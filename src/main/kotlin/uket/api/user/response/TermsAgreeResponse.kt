package uket.api.user.response

import uket.domain.terms.entity.TermSign

data class TermsAgreeResponse(
    val termId: Long,
    val isAgreed: Boolean,
    val documentVersion: Long,
) {
    companion object {
        fun from(termSign: TermSign): TermsAgreeResponse = TermsAgreeResponse(
            termSign.terms.id,
            termSign.isAgreed,
            termSign.documentVersion
        )
    }
}
