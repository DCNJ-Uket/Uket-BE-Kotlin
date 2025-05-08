package uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.api.admin.dto.EventNameDto
import uket.common.LoggerDelegate
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.repository.UketEventRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class UketEventService(
    private val uketEventRepository: UketEventRepository,
) {
    private val log by LoggerDelegate()

    @Transactional(readOnly = true)
    fun getById(uketEventId: Long): UketEvent {
        val uketEvent = uketEventRepository.findByIdOrNull(uketEventId)
            ?: throw IllegalStateException("해당 행사를 찾을 수 없습니다.")
        return uketEvent
    }

    @Transactional(readOnly = true)
    fun getDetailById(uketEventId: Long): UketEvent {
        val uketEvent = uketEventRepository.findByIdAndLastRoundDateAfterNowWithBanners(uketEventId)
            ?: throw IllegalStateException("해당 행사를 찾을 수 없습니다.")
        return uketEvent
    }

    @Transactional(readOnly = true)
    fun getOrganizationNameByUketEventIid(uketEventId: Long): String {
        val name = uketEventRepository.findOrganizationNameByUketEventId(uketEventId)
            ?: throw IllegalStateException("해당 행사의 이름을 찾을 수 없습니다.")
        return name
    }

    @Transactional(readOnly = true)
    fun getEventsByOrganizationId(organizationId: Long): List<EventNameDto> {
        val events = uketEventRepository.findAllByOrganizationId(organizationId)
        return events.map { event -> EventNameDto.of(organizationId, event) }
    }

    @Transactional(readOnly = true)
    fun findAllNowActiveOrdered(at: LocalDateTime): List<UketEvent> =
        uketEventRepository.findAllByLastRoundDateAfterOrderByFirstRoundDateTime(at.truncatedTo(ChronoUnit.DAYS))

    @Transactional(readOnly = true)
    fun findAllNowActiveOrderedFestival(at: LocalDateTime): List<UketEvent> =
        uketEventRepository.findAllFestivalByLastRoundDateAfterOrderByFirstRoundDateTime(at.truncatedTo(ChronoUnit.DAYS))

    @Transactional(readOnly = true)
    fun findAllNowActiveOrderedPerformance(at: LocalDateTime): List<UketEvent> =
        uketEventRepository.findAllPerformanceByLastRoundDateAfterOrderByFirstRoundDateTime(at.truncatedTo(ChronoUnit.DAYS))
}
