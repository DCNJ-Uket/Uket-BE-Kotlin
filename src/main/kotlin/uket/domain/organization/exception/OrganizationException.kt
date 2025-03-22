package uket.uket.domain.organization.exception

import uket.uket.common.BaseException
import uket.uket.common.ErrorCode

class OrganizationException(
    errorCode: ErrorCode,
) : BaseException(errorCode)
