package uket.uket.domain.terms.dto

import uket.domain.terms.entity.Document
import uket.domain.terms.entity.Terms
import uket.domain.terms.enums.TermsType

data class CheckRequiredTerms(
    val termsId: Long,
    val name: String,
    val termsType: TermsType,
    val link: String,
    val version: Long,
) {
    companion object {
        fun of(it: Terms, document: Document): CheckRequiredTerms = CheckRequiredTerms(
            termsId = it.id,
            name = it.name,
            termsType = it.termsType,
            link = document.link,
            version = document.version
        )
    }
}
