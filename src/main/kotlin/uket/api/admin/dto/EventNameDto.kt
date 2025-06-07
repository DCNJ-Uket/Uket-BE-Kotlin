package uket.api.admin.dto

import uket.domain.uketevent.entity.UketEvent

data class EventNameDto(
    val organizationId: Long,
    val eventId: Long,
    val eventName: String,
) {
    companion object {
        fun of(organizationId: Long, event: UketEvent): EventNameDto {
            return EventNameDto(
                organizationId = organizationId,
                eventId = event.id,
                eventName = event.eventName
            )
        }
    }
}
