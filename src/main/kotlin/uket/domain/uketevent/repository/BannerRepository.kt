package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.domain.uketevent.entity.Banner

interface BannerRepository : JpaRepository<Banner, Long> {
    fun findAllByUketEventId(uketEventId: Long): List<Banner>
}
