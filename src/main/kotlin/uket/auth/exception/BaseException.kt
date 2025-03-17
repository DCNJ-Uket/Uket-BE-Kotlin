package uket.auth.exception

data class BaseException(val errorCode: ErrorCode) : RuntimeException(errorCode.message)
