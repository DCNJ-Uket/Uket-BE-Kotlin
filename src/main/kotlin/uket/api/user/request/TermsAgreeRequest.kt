package uket.api.user.request

data class TermsAgreeRequest(
    val termsId: Long,
    val isAgree: Boolean,
    val version: Long,
)
