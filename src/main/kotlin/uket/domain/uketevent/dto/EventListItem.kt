package uket.domain.uketevent.dto

import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.enums.TicketingStatus
import java.time.LocalDateTime

data class EventListItem(
    val eventId: Long,
    val eventName: String,
    val eventThumbnailImagePath: String,
    val eventStartDate: LocalDateTime,
    val eventEndDate: LocalDateTime,
    val ticketingStartDate: LocalDateTime,
    val ticketingEndDate: LocalDateTime,
    val ticketingStatus: TicketingStatus,
) {
    companion object {
        fun of(event: UketEvent, ticketingStatus: TicketingStatus, uketEventRounds: List<UketEventRound>): EventListItem = EventListItem(
            eventId = event.id,
            eventName = event.eventName,
            eventThumbnailImagePath = event.thumbnailImageId,
            eventStartDate = event.firstRoundDateTime,
            eventEndDate = event.lastRoundDateTime,
            ticketingStartDate = uketEventRounds.minOf { it.ticketingStartDateTime },
            ticketingEndDate = uketEventRounds.maxOf { it.ticketingEndDateTime },
            ticketingStatus = ticketingStatus
        )
    }
}
