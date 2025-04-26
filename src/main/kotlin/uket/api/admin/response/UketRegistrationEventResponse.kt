package uket.api.admin.response

import domain.eventregistration.EventData
import uket.common.enums.EventType
import uket.domain.eventregistration.entity.EventRegistration

data class UketRegistrationEventResponse(
    val eventType: EventType,
    val festivalData: EventData? = null,
    val performanceData: EventData? = null,
) {
    companion object {
        fun from(eventRegistration: EventRegistration): UketRegistrationEventResponse {
            val eventData = EventData.from(eventRegistration)

            return when (eventRegistration.eventType) {
                EventType.FESTIVAL -> festival(eventData)
                EventType.PERFORMANCE -> performance(eventData)
            }
        }

        private fun festival(eventData: EventData) =
            UketRegistrationEventResponse(
                eventType = EventType.FESTIVAL,
                festivalData = eventData
            )

        private fun performance(eventData: EventData) =
            UketRegistrationEventResponse(
                eventType = EventType.PERFORMANCE,
                performanceData = eventData
            )
    }
}
