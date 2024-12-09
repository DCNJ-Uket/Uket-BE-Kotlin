package uket.gateway.exception

data class BaseException(val errorCode: ErrorCode) : RuntimeException(errorCode.message)
