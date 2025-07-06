package uket.api.common

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import uket.facade.S3ImageFacade
import uket.modules.s3.service.S3Service

@Tag(name = "이미지 관련 API", description = "이미지 관련 API 입니다.")
@RestController
class ImageController(
    private val s3ImageFacade: S3ImageFacade,
    private val s3Service: S3Service,
) {
    @Operation(summary = "이미지 조회", description = "이미지 id를 이용해 해당 image를 서버로부터 넘겨받습니다.")
    @GetMapping("/image/{imageId}")
    fun getImage(
        @PathVariable("imageId") imageId: Long,
    ): ResponseEntity<ByteArray> {
        val (imageBytes, contentType) = s3Service.getImage(imageId)

        val headers = HttpHeaders().apply {
            this.contentType = MediaType.parseMediaType(contentType)
        }

        return ResponseEntity.ok()
            .headers(headers)
            .body(imageBytes)
    }
}
