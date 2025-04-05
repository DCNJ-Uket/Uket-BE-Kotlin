package uket.uket.api.response

import uket.uket.common.enums.EventType

data class RegisterUketEventResponse(
    val uketEventRegistrationId: Long,
    val eventType: EventType,
)
