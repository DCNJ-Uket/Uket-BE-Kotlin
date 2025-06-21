package uket.domain.terms.dto

import uket.domain.terms.entity.TermSign
import uket.domain.terms.entity.Terms

data class OptionalTermAnswer(
    val termsId: Long,
    val name: String,
    val documentId: Long,
    val isAgreed: Boolean,
) {
    companion object {
        fun of(terms: Terms, termSign: TermSign): OptionalTermAnswer = OptionalTermAnswer(
            termsId = terms.id,
            name = terms.name,
            documentId = termSign.document.id,
            isAgreed = termSign.isAgreed
        )
    }
}
