package uket.api.user.response

import uket.domain.terms.entity.TermSign

data class TermsAgreeResponse(
    val termId: Long,
    val isAgreed: Boolean,
    val documentId: Long,
) {
    companion object {
        fun from(termSign: TermSign): TermsAgreeResponse = TermsAgreeResponse(
            termId = termSign.terms.id,
            isAgreed = termSign.isAgreed,
            documentId = termSign.document.id
        )
    }
}
