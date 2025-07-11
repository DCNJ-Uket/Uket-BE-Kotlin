package uket.api.user.response

import uket.common.enums.EventType
import uket.domain.admin.entity.Organization
import uket.domain.uketevent.entity.Banner
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.enums.TicketingStatus
import java.time.LocalDateTime

data class EventDetailResponse(
    val eventId: Long,
    val eventName: String,
    val eventType: EventType,
    val firstRoundStartDateTime: LocalDateTime,
    val lastRoundStartDateTime: LocalDateTime,
    val ticketingStartDateTime: LocalDateTime,
    val ticketingEndDateTime: LocalDateTime,
    val information: String,
    val detailImageId: String,
    val banners: List<EventDetailBannerDto>,
    val caution: String,
    val organizationName: String,
    val contact: UketEvent.EventContact,
    val location: String,
    val ticketingStatus: TicketingStatus,
) {
    data class EventDetailBannerDto(
        val imageId: Long,
        val link: String,
    ) {
        companion object {
            fun from(banner: Banner): EventDetailBannerDto = EventDetailBannerDto(
                imageId = banner.imageId,
                link = banner.link
            )
        }
    }

    companion object {
        fun of(
            uketEvent: UketEvent,
            uketEventRound: UketEventRound,
            organization: Organization,
            banners: List<Banner>,
            ticketingStatus: TicketingStatus,
        ): EventDetailResponse = EventDetailResponse(
            eventId = uketEvent.id,
            eventName = uketEvent.eventName,
            eventType = uketEvent.eventType,
            firstRoundStartDateTime = uketEvent.firstRoundDateTime,
            lastRoundStartDateTime = uketEvent.lastRoundDateTime,
            ticketingStartDateTime = uketEventRound.ticketingStartDateTime,
            ticketingEndDateTime = uketEventRound.ticketingEndDateTime,
            location = uketEvent.location,
            banners = banners.map { EventDetailBannerDto.from(it) },
            information = uketEvent.details.information,
            detailImageId = uketEvent.eventImageId,
            caution = uketEvent.details.caution,
            organizationName = organization.name,
            contact = uketEvent.details.contact,
            ticketingStatus = ticketingStatus
        )
    }
}
