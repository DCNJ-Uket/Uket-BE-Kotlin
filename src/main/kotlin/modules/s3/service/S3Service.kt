package uket.modules.s3.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetUrlRequest
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import uket.modules.s3.properties.S3Properties
import java.time.Duration

@Service
class S3Service(
    private val preSigner: S3Presigner,
    private val s3Properties: S3Properties,
    private val s3Client: S3Client
) {
    companion object {
        private const val IMAGE_FOLDER = "images"
    }

    fun getImage(filename: String): String? =
        getPreSignedUrl(IMAGE_FOLDER, filename)

    fun putImage(file: MultipartFile, filename: String): String? {
        val objectRequest = putObjectRequest(file, IMAGE_FOLDER, filename)
        s3Client.putObject(objectRequest, RequestBody.fromBytes(file.getBytes()))
        return s3Client.utilities().getUrl { builder: GetUrlRequest.Builder ->
            builder.bucket(
                s3Properties.bucket
            ).key(listOf(IMAGE_FOLDER, filename).joinToString("/"))
        }.toExternalForm()
    }
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

    private fun putObjectRequest(uploadFile: MultipartFile, folder: String, filename: String): PutObjectRequest {
        return PutObjectRequest.builder()
            .bucket(s3Properties.bucket)
            .key(java.lang.String.join("/", folder, filename))
            .contentType(uploadFile.contentType)
            .contentLength(uploadFile.size)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .build()
    }
}
