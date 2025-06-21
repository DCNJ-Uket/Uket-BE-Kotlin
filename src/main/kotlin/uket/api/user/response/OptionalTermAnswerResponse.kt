package uket.api.user.response

import uket.domain.terms.dto.OptionalTermAnswer

data class OptionalTermAnswerResponse(
    val termsId: Long,
    val name: String,
    val documentId: Long,
    val isAgreed: Boolean,
) {
    companion object {
        fun of(answer: OptionalTermAnswer): OptionalTermAnswerResponse = OptionalTermAnswerResponse(
            termsId = answer.termsId,
            name = answer.name,
            documentId = answer.documentId,
            isAgreed = answer.isAgreed
        )
    }
}
