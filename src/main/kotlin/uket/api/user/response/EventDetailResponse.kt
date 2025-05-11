package uket.api.user.response

import uket.domain.uketevent.entity.Banner
import uket.domain.uketevent.entity.UketEvent

data class EventDetailResponse(
    val eventName: String,
    val banners: List<EventDetailBannerDto>,
    val information: String,
    val caution: String,
    val location: String,
    val contactType: UketEvent.EventContact.ContactType,
    val contactContent: String,
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
        fun from(uketEvent: UketEvent): EventDetailResponse = EventDetailResponse(
            eventName = uketEvent.eventName,
            banners = uketEvent.banners.map { EventDetailBannerDto.from(it) },
            information = uketEvent.details.information,
            caution = uketEvent.details.caution,
            location = uketEvent.location,
            contactType = uketEvent.details.contact.type,
            contactContent = uketEvent.details.contact.content
        )
    }
}
