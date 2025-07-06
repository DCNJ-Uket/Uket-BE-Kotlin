package uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.api.admin.dto.EventNameDto
import uket.common.enums.EventType
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.repository.UketEventRepository

@Service
class UketEventService(
    private val uketEventRepository: UketEventRepository,
) {
    @Transactional(readOnly = true)
    fun getById(uketEventId: Long): UketEvent {
        val uketEvent = uketEventRepository.findByIdOrNull(uketEventId)
            ?: throw IllegalStateException("해당 행사를 찾을 수 없습니다.")
        return uketEvent
    }

    @Transactional(readOnly = true)
    fun getDetailById(uketEventId: Long): UketEvent {
        val uketEvent = uketEventRepository.findVisibleOneById(uketEventId)
            ?: throw IllegalStateException("해당 행사를 찾을 수 없습니다.")
        return uketEvent
    }

    @Transactional(readOnly = true)
    fun getEventsByOrganizationId(organizationId: Long): List<EventNameDto> {
        val events = uketEventRepository.findAllByOrganizationIdOrderByCreatedAtDesc(organizationId)
        return events.map { event -> EventNameDto.of(organizationId, event) }
    }

    @Transactional(readOnly = true)
    fun findAll(): List<EventNameDto> {
        val events = uketEventRepository.findAllByOrderByCreatedAtDesc()
        return events.map { event -> EventNameDto.of(event.organizationId, event) }
    }

    @Transactional(readOnly = true)
    fun findAllVisibleOrderedEventByEventTypes(eventTypes: List<EventType>): List<UketEvent> =
        uketEventRepository.findAllVisibleInEventTypesOrderByFirstRoundDateTime(eventTypes)

    @Transactional
    fun deleteById(id: Long) {
        uketEventRepository.deleteById(id)
    }

    @Transactional
    fun save(uketEvent: UketEvent): UketEvent = uketEventRepository.save(uketEvent)

    @Transactional
    fun init(uketEventId: Long): UketEvent {
        val uketEvent = getById(uketEventId)

        uketEvent.init()

        return uketEventRepository.save(uketEvent)
    }

    @Transactional
    fun open(uketEventId: Long): UketEvent {
        val uketEvent = getById(uketEventId)

        uketEvent.open()

        return uketEventRepository.save(uketEvent)
    }

    @Transactional
    fun finish(uketEventId: Long): UketEvent {
        val uketEvent = getById(uketEventId)

        uketEvent.finish()

        return uketEventRepository.save(uketEvent)
    }
}
