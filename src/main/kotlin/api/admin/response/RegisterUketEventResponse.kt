package uket.api.admin.response

import uket.common.enums.EventType

data class RegisterUketEventResponse(
    val uketEventRegistrationId: Long,
    val eventType: EventType,
)
