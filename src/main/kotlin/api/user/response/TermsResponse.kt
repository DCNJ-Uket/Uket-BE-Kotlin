package uket.api.user.response

import uket.domain.terms.entity.Terms
import uket.domain.terms.enums.TermsType

data class TermsResponse(
    val termsId: Long,
    val name: String,
    val type: TermsType,
    val link: String,
    val isAgreed: Boolean,
) {
    companion object {
        fun of(terms: Terms, isAgreed: Boolean, link: String): TermsResponse = TermsResponse(
            termsId = terms.id,
            name = terms.name,
            type = terms.termsType,
            link = link,
            isAgreed = isAgreed
        )
    }
}
