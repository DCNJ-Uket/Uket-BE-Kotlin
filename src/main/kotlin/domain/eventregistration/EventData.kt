package domain.eventregistration

import uket.domain.eventregistration.entity.EventRegistration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class EventData(
    val eventName: String,
    val location: String,
    val eventRound: List<EventRoundDto>,
    val ticketingStartDateTime: LocalDateTime, // yyyy-MM-ddThh:mm:ss
    val ticketingEndDateTime: LocalDateTime, // yyyy-MM-ddThh:mm:ss
    val entryGroup: List<EntryGroupDto>,
    val totalTicketCount: Int,
    val details: EventDetailsDto,
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

    data class EventDetailsDto(
        val information: String,
        val caution: String,
    )

    data class ContactInfoDto(
        val type: String, // 예: INSTAGRAM
        val content: String, // 예: @soritor
    )

    companion object {
        fun from(eventRegistration: EventRegistration): EventData {
            return with(eventRegistration) {
                EventData(
                    eventName = eventName,
                    location = location,
                    eventRound = eventRound.map {
                        EventRoundDto(
                            date = it.eventRoundDate,
                            startTime = it.eventStartTime
                        )
                    },
                    ticketingStartDateTime = ticketingStartDateTime,
                    ticketingEndDateTime = ticketingEndDateTime,
                    entryGroup = entryGroup.map {
                        EntryGroupDto(
                            ticketCount = it.ticketCount,
                            entryStartTime = it.entryStartTime,
                            entryEndTime = it.entryEndTime
                        )
                    },
                    totalTicketCount = totalTicketCount,
                    details = EventDetailsDto(
                        information = details.information,
                        caution = details.caution
                    ),
                    contact = ContactInfoDto(
                        type = details.contact.type.name,
                        content = details.contact.content,
                    ),
                    uketEventImageId = uketEventImageId,
                    thumbnailImageId = thumbnailImageId,
                    bannerImageIds = bannerImageIds,
                )
            }
        }
    }
}
