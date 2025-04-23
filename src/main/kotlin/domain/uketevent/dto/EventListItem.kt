package uket.domain.uketevent.dto

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
)
