package uket.common.response

import uket.common.ErrorCode

data class ErrorResponse(
    val message: String,
) {
    companion object {
        fun from(message: String): ErrorResponse {
            return ErrorResponse(message = message)
        }

        fun from(errorCode: ErrorCode): ErrorResponse {
            return ErrorResponse(message = errorCode.message)
        }
    }
}
