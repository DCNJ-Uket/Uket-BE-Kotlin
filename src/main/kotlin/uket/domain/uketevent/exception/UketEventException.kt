package uket.uket.domain.uketevent.exception

import uket.uket.common.BaseException
import uket.uket.common.ErrorCode

class UketEventException(
    errorCode: ErrorCode,
) : BaseException(errorCode)
