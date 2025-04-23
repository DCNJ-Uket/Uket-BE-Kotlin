package uket.modules.s3.service

import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import uket.modules.s3.properties.S3Properties
import java.time.Duration

@Service
class S3Service(
    private val preSigner: S3Presigner,
    private val s3Properties: S3Properties,
) {
    companion object {
        private const val UNIVERSITY_LOGO_FOLDER = "university-logo"
        private const val BANNER_IMAGE_FOLDER = "banner"
        private const val EVENT_MAIN_IMAGE_FOLDER = "event"
    }

    fun getUniversityLogo(filename: String?): String? =
        getPreSignedUrl(UNIVERSITY_LOGO_FOLDER, filename)

    fun getBannerImage(filename: String?): String? =
        getPreSignedUrl(BANNER_IMAGE_FOLDER, filename)

    fun getEventMainImage(filename: String?): String? =
        getPreSignedUrl(EVENT_MAIN_IMAGE_FOLDER, filename)

    private fun getPreSignedUrl(folder: String, filename: String?): String? {
        if (filename.isNullOrEmpty()) return null

        val url = preSigner.presignGetObject(getObjectPresignRequest(folder, filename)).url().toString()
        preSigner.close()
        return url
    }

    private fun getObjectPresignRequest(folder: String, filename: String): GetObjectPresignRequest {
        return GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(1))
            .getObjectRequest {
                it.bucket(s3Properties.bucket)
                    .key("$folder/$filename")
            }
            .build()
    }
}
