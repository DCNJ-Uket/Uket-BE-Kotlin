package uket.domain.uketevent.dto

import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.enums.TicketingStatus
import java.time.LocalDateTime

data class EventListItem(
    val eventName: String,
    val eventThumbnailImagePath: String,
    val eventStartDate: LocalDateTime,
    val eventEndDate: LocalDateTime,
    val ticketingStartDate: LocalDateTime,
    val ticketingEndDate: LocalDateTime,
    val ticketingStatus: TicketingStatus,
) {
    companion object {
        fun of(event: UketEvent, status: TicketingStatus): EventListItem = EventListItem(
            eventName = event.eventName,
            eventThumbnailImagePath = event.thumbnailImageId,
            eventStartDate = event.firstRoundDateTime,
            eventEndDate = event.lastRoundDateTime,
            ticketingStartDate = event.ticketingStartDateTime,
            ticketingEndDate = event.ticketingEndDateTime,
            ticketingStatus = status
        )
    }
}
