package uket.api.admin.response

data class EventImageUploadResponse(
    val uketEventImageId: String,
    val thumbnailImageId: String,
    val bannerImageIds: String,
)
