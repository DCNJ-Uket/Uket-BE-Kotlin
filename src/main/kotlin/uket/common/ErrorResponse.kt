package uket.common

data class ErrorResponse(
    val code: String,
    val message: String,
) {
    companion object {
        fun of(code: String, message: String): ErrorResponse = ErrorResponse(code, message)

        fun of(errorCode: ErrorCode, exception: Throwable? = null): ErrorResponse = ErrorResponse(
            errorCode.code,
            exception?.message ?: errorCode.message,
        )
    }
}
