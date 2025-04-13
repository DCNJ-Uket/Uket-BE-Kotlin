package uket.api.admin.request

import domain.eventregistration.EventData
import uket.common.enums.EventType
import uket.domain.eventregistration.entity.EntryGroupRegistration
import uket.domain.eventregistration.entity.EventRegistration
import uket.domain.eventregistration.entity.EventRegistration.EventContact.ContactType
import uket.domain.eventregistration.entity.EventRoundRegistration

data class RegisterUketEventRequest(
    val festivalData: EventData? = null,
    val performanceData: EventData? = null,
) {
    fun validateByEventType(eventType: EventType) {
        when (eventType) {
            EventType.FESTIVAL -> require(festivalData != null) {
                "[RegisterUketEventRequest] festivalData 가 null일 수 없습니다. | $eventType"
            }

            EventType.PERFORMANCE -> require(performanceData != null) {
                "[RegisterUketEventRequest] performanceData 가 null일 수 없습니다. | $eventType"
            }
        }
    }

    fun toEntity(organizationId: Long, eventType: EventType): EventRegistration {
        val eventData = when (eventType) {
            EventType.FESTIVAL -> this.festivalData!!
            EventType.PERFORMANCE -> this.performanceData!!
        }

        return with(eventData) {
            EventRegistration(
                eventType = eventType,
                eventName = eventName,
                organizationId = organizationId,
                location = location,
                ticketingStartDateTime = ticketingStartDateTime,
                ticketingEndDateTime = ticketingEndDateTime,
                totalTicketCount = totalTicketCount,
                details = EventRegistration.EventDetails(
                    information = details.information,
                    caution = details.caution,
                    contact = EventRegistration.EventContact(
                        type = ContactType.entries.find { it.name == contact.type }
                            ?: error("[RegisterUketEventRequest] ContactType을 찾을 수 없습니다."),
                        content = contact.content,
                    ),
                ),
                uketEventImageId = uketEventImageId,
                thumbnailImageId = thumbnailImageId,
                bannerImageIds = bannerImageIds.map { it },
                _eventRound = eventRound.map {
                    EventRoundRegistration(
                        eventRoundDate = it.date,
                        eventStartTime = it.startTime,
                    )
                },
                _entryGroup = entryGroup.map {
                    EntryGroupRegistration(
                        entryStartTime = it.entryStartTime,
                        entryEndTime = it.entryEndTime,
                        ticketCount = it.ticketCount,
                    )
                },
            )
        }
    }
}
