package uket.common.response

import uket.common.ErrorLevel
import uket.common.LoggerDelegate

const val DEFAULT_PUBLIC_ERROR_TITLE = "잠시 후 다시 시도해 주세요!"
const val DEFAULT_PUBLIC_ERROR_MESSAGE = "이용에 불편을 드려 죄송합니다.\n네트워크 또는 데이터 오류로 페이지를 불러오지 못하였습니다.\n잠시 후 다시 시도해 주세요."

data class PublicErrorResponse(
    val title: String? = DEFAULT_PUBLIC_ERROR_TITLE,
    val message: String? = DEFAULT_PUBLIC_ERROR_MESSAGE,
    val errorLevel: ErrorLevel = ErrorLevel.ERROR,
) {
    companion object {
        private val log by LoggerDelegate()

        fun withErrorLevel(
            title: String? = null,
            publicMessage: String? = null,
            systemMessage: String? = null,
            errorLevel: ErrorLevel,
            exception: Throwable,
        ): PublicErrorResponse {
            return when (errorLevel) {
                ErrorLevel.TRACE -> trace(title, publicMessage, systemMessage, exception)
                ErrorLevel.DEBUG -> debug(title, publicMessage, systemMessage, exception)
                ErrorLevel.INFO -> info(title, publicMessage, systemMessage, exception)
                ErrorLevel.WARN -> warn(title, publicMessage, systemMessage, exception)
                ErrorLevel.ERROR -> error(title, publicMessage, systemMessage, exception)
            }
        }

        fun trace(
            title: String? = null,
            publicMessage: String? = null,
            systemMessage: String? = null,
            exception: Throwable,
        ): PublicErrorResponse {
            log.trace(systemMessage ?: exception.message, exception)

            return PublicErrorResponse(
                title = title,
                message = publicMessage,
                errorLevel = ErrorLevel.TRACE
            )
        }

        fun debug(
            title: String? = null,
            publicMessage: String? = null,
            systemMessage: String? = null,
            exception: Throwable,
        ): PublicErrorResponse {
            log.debug(systemMessage ?: exception.message, exception)

            return PublicErrorResponse(
                title = title,
                message = publicMessage,
                errorLevel = ErrorLevel.DEBUG
            )
        }

        fun info(
            title: String? = null,
            publicMessage: String? = null,
            systemMessage: String? = null,
            exception: Throwable,
        ): PublicErrorResponse {
            log.info(systemMessage ?: exception.message, exception)

            return PublicErrorResponse(
                title = title,
                message = publicMessage,
                errorLevel = ErrorLevel.INFO
            )
        }

        fun warn(
            title: String? = null,
            publicMessage: String? = null,
            systemMessage: String? = null,
            exception: Throwable,
        ): PublicErrorResponse {
            log.warn(systemMessage ?: exception.message, exception)

            return PublicErrorResponse(
                title = title,
                message = publicMessage,
                errorLevel = ErrorLevel.WARN
            )
        }

        fun error(
            title: String? = null,
            publicMessage: String? = null,
            systemMessage: String? = null,
            exception: Throwable,
        ): PublicErrorResponse {
            log.error(systemMessage ?: exception.message, exception)

            return PublicErrorResponse(
                title = title,
                message = publicMessage,
                errorLevel = ErrorLevel.ERROR
            )
        }
    }
}
