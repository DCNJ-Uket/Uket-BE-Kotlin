package uket.domain.terms.dto

data class TermsAgreeAnswer(
    val termsId: Long,
    val isAgree: Boolean,
    val documentId: Long,
)
