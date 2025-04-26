package uket.api.user.response

import uket.domain.terms.enums.TermsType
import uket.uket.domain.terms.dto.CheckRequiredTerms

data class TermsResponse(
    val termsId: Long,
    val name: String,
    val type: TermsType,
    val link: String,
    val isAgreed: Boolean,
    val version: Long,
) {
    companion object {
        fun of(terms: CheckRequiredTerms): TermsResponse = TermsResponse(
            termsId = terms.termsId,
            name = terms.name,
            type = terms.termsType,
            link = terms.link,
            isAgreed = false,
            version = terms.version
        )
    }
}
