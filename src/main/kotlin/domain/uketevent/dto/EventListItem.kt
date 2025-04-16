package uket.domain.uketevent.dto

import java.time.LocalDateTime

data class EventListItem(
    private val eventName: String,
    private val eventThumbnailImagePath: String,
    private val eventStartDate: LocalDateTime,
    private val eventEndDate: LocalDateTime,
    private val ticketingStartDate: LocalDateTime,
    private val ticketingEndDate: LocalDateTime,
) {
//    fun from(uketEvent: UketEvent): EventListItem {
//        return EventListItem(
//            eventName = uketEvent.eventName,
//            eventThumbnailImagePath = uketEvent.thumbnailImageId,
//            eventStartDate = uketEvent.,
//            eventEndDate =,
//            ticketingStartDate = uketEvent.ticketingStartDateTime,
//            ticketingEndDate = uketEvent.ticketingEndDateTime
//        )
//    }
}
