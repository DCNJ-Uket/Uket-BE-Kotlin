package uket.facade

import org.springframework.stereotype.Service
import uket.domain.uketevent.repository.UketEventRepository
import uket.modules.s3.service.S3Service

@Service
class S3ImageFacade(
    private val s3Service: S3Service,
) {
}
