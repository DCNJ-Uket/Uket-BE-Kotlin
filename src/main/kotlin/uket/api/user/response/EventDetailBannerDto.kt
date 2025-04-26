package uket.api.user.response

import uket.domain.uketevent.entity.Banner

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
