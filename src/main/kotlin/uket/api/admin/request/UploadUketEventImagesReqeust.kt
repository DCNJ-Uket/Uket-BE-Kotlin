package uket.api.admin.request

import org.springframework.web.multipart.MultipartFile

data class UploadUketEventImagesReqeust(
    val eventImage: MultipartFile?,
    val thumbnailImage: MultipartFile?,
    val bannerImages: List<MultipartFile>?,
)
