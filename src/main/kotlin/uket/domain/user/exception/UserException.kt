package uket.uket.domain.user.exception

import uket.uket.common.BaseException
import uket.uket.common.ErrorCode

class UserException(
    errorCode: ErrorCode,
) : BaseException(errorCode)
