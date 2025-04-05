package uket.uket.api.request

import uket.domain.eventregistration.entity.EntryGroupRegistration
import uket.domain.eventregistration.entity.EventRegistration
import uket.domain.eventregistration.entity.EventRegistration.EventContact.ContactType
import uket.domain.eventregistration.entity.EventRoundRegistration
import uket.uket.common.enums.EventType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class RegisterUketEventRequest(
    val festivalData: EventData? = null,
    val performanceData: EventData? = null,
) {
    fun validateByEventType(eventType: EventType) {
        when (eventType) {
            EventType.FESTIVAL -> require(festivalData != null) {
                "[EventRegistrationController] festivalData 가 null일 수 없습니다. | $eventType"
            }

            EventType.PERFORMANCE -> require(performanceData != null) {
                "[EventRegistrationController] performanceData 가 null일 수 없습니다. | $eventType"
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
                        type = ContactType.valueOf(contact.type),
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

data class EventData(
    val eventName: String,
    val location: String,
    val eventRound: List<EventRoundDto>,
    val ticketingStartDateTime: LocalDateTime, // yyyy-MM-ddThh:mm:ss
    val ticketingEndDateTime: LocalDateTime, // yyyy-MM-ddThh:mm:ss
    val entryGroup: List<EntryGroupDto>,
    val totalTicketCount: Int,
    val details: FestivalDetailsDto,
    val contact: ContactInfoDto,
    val uketEventImageId: String,
    val thumbnailImageId: String,
    val bannerImageIds: List<String>,
) {
    data class EventRoundDto(
        val date: LocalDate, // yyyy-MM-dd
        val startTime: LocalTime, // hh:mm:ss
    )

    data class EntryGroupDto(
        val ticketCount: Int,
        val entryStartTime: LocalTime, // hh:mm:ss
        val entryEndTime: LocalTime, // hh:mm:ss
    )

    data class FestivalDetailsDto(
        val information: String,
        val caution: String,
    )

    data class ContactInfoDto(
        val type: String, // 예: INSTAGRAM
        val content: String, // 예: @soritor
    )
}
