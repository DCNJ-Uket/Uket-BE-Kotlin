package uket.common

open class BaseException(
    val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)
