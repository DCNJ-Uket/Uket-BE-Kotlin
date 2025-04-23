package uket.domain.images.respository

import org.springframework.data.jpa.repository.JpaRepository
import uket.domain.images.entity.Image

interface ImageRepository : JpaRepository<Image, Long> {

}
