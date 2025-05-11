package uket.api.user.response

import uket.common.enums.EventType
import uket.domain.admin.entity.Organization
import uket.domain.uketevent.entity.Banner
import uket.domain.uketevent.entity.UketEvent

data class EventDetailResponse(
    val eventId: Long,
    val eventName: String,
    val eventType: EventType,
    val location: String,
    val banners: List<EventDetailBannerDto>,
    val information: String,
    val detailImagePath: String,
    val caution: String,
    val organizationName: String,
    val contact: UketEvent.EventContact,
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
        fun from(uketEvent: UketEvent, organization: Organization): EventDetailResponse = EventDetailResponse(
            eventId = uketEvent.id,
            eventName = uketEvent.eventName,
            eventType = uketEvent.eventType,
            location = uketEvent.location,
            banners = uketEvent.banners.map { EventDetailBannerDto.from(it) },
            information = uketEvent.details.information,
            detailImagePath = uketEvent.eventImageId,
            caution = uketEvent.details.caution,
            organizationName = organization.name,
            contact = uketEvent.details.contact
        )
    }
}
