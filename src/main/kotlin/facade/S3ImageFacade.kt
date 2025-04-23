package uket.facade

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import uket.api.admin.response.EventImageUploadResponse
import uket.domain.images.entity.Image
import uket.domain.images.respository.ImageRepository
import uket.modules.s3.service.S3Service
import java.util.concurrent.CompletableFuture

@Service
class S3ImageFacade(
    private val imageRepository: ImageRepository,
    private val s3Service: S3Service,
) {
    fun uploadUketEventImages(eventImage: MultipartFile?, thumbnailImage: MultipartFile?, bannerImages: List<MultipartFile>?): EventImageUploadResponse {
        val tasks = mutableListOf<CompletableFuture<Pair<String, Long>?>>()

        eventImage?.let {
            tasks.add(CompletableFuture.supplyAsync {
                val image = imageRepository.save(Image())
                s3Service.putImage(it, image.id.toString())
                "event" to image.id
            })
        }

        thumbnailImage?.let {
            tasks.add(CompletableFuture.supplyAsync {
                val image = imageRepository.save(Image())
                s3Service.putImage(it, image.id.toString())
                "thumbnail" to image.id
            })
        }

        bannerImages?.forEach { file ->
            tasks.add(CompletableFuture.supplyAsync {
                val image = imageRepository.save(Image())
                s3Service.putImage(file, image.id.toString())
                "banner" to image.id
            })
        }

        val results = tasks.mapNotNull { it.join() }
        val eventImageId = results.find { it.first == "event" }?.second?.toString() ?: ""
        val thumbnailImageId = results.find { it.first == "thumbnail" }?.second?.toString() ?: ""
        val bannerImageIds = results.filter { it.first == "banner" }
            .map { it.second }
            .joinToString(",")

        return EventImageUploadResponse(
            uketEventImageId = eventImageId,
            thumbnailImageId = thumbnailImageId,
            bannerImageIds = bannerImageIds
        )
    }
}
