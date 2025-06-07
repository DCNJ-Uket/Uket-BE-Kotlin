package uket.api.user.response

import uket.common.enums.EventType
import uket.domain.admin.entity.Organization
import uket.domain.uketevent.entity.Banner
import uket.domain.uketevent.entity.UketEvent
import java.time.LocalDateTime

data class EventDetailResponse(
    val eventId: Long,
    val eventName: String,
    val eventType: EventType,
    val firstRoundStartDateTime: LocalDateTime,
    val lastRoundStartDateTime: LocalDateTime,
    val information: String,
    val detailImageId: String,
    val banners: List<EventDetailBannerDto>,
    val caution: String,
    val organizationName: String,
    val contact: UketEvent.EventContact,
    val location: String,
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
            organization: Organization,
            banners: List<Banner>
        ): EventDetailResponse = EventDetailResponse(
            eventId = uketEvent.id,
            eventName = uketEvent.eventName,
            eventType = uketEvent.eventType,
            firstRoundStartDateTime = uketEvent.firstRoundDateTime,
            lastRoundStartDateTime = uketEvent.firstRoundDateTime,
            location = uketEvent.location,
            banners = banners.map { EventDetailBannerDto.from(it) },
            information = uketEvent.details.information,
            detailImageId = uketEvent.eventImageId,
            caution = uketEvent.details.caution,
            organizationName = organization.name,
            contact = uketEvent.details.contact
        )
    }
}
