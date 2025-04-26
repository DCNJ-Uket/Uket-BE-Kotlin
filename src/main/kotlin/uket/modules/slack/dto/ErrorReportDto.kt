package uket.modules.slack.dto

data class ErrorReportDto(
    val errorMessage: String?,
    val payload: String,
)
