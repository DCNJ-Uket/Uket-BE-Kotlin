package uket.api.admin.response

import uket.common.toResponseFormat
import uket.domain.eventregistration.entity.EventRegistrationStatus

data class ChangeEventRegistrationStatusResponse private constructor(
    val uketEventRegistrationId: Long,
    val currentStatus: String,
) {
    constructor(
        uketEventRegistrationId: Long,
        currentStatus: EventRegistrationStatus,
    ) : this(uketEventRegistrationId = uketEventRegistrationId, currentStatus = currentStatus.toResponseFormat())
}
