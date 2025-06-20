package uket.api.user.response

import uket.domain.terms.dto.CheckRequiredTerms
import uket.domain.terms.enums.TermsType

data class TermsResponse(
    val termsId: Long,
    val name: String,
    val type: TermsType,
    val link: String,
    val isAgreed: Boolean,
    val documentId: Long,
    val documentNo: Long,
    val documentVersion: Long,
) {
    companion object {
        fun of(terms: CheckRequiredTerms): TermsResponse = TermsResponse(
            termsId = terms.termsId,
            name = terms.name,
            type = terms.termsType,
            link = terms.link,
            isAgreed = false,
            documentId = terms.documentId,
            documentNo = terms.documentNo,
            documentVersion = terms.documentVersion
        )
    }
}
