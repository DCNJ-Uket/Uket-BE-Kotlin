package uket.domain.uketevent.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.uketevent.entity.Banner
import uket.domain.uketevent.repository.BannerRepository

@Service
class BannerService(
    private val bannerRepository: BannerRepository,
) {
    @Transactional
    fun saveAll(banners: List<Banner>): List<Banner> {
        return bannerRepository.saveAll(banners)
    }
}
