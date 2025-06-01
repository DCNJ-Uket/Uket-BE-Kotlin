package uket.domain.eventregistration.service

import org.hibernate.Hibernate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.common.PublicException
import uket.domain.eventregistration.entity.EventRegistration
import uket.domain.eventregistration.entity.EventRegistrationStatus

@Service
class EventRegistrationService(
    val eventRegistrationRepository: EventRegistrationRepository,
) {
    @Transactional(readOnly = true)
    fun findById(id: Long): EventRegistration? {
        return eventRegistrationRepository.findByIdOrNull(id)
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): EventRegistration = findById(id)
        ?: throw IllegalArgumentException("[EventRegistrationService] EventRegistration을 조회할 수 없습니다. | eventRegistrationId: $id")

    @Transactional(readOnly = true)
    fun findAllByOrganizationId(pageable: Pageable, organizationId: Long): Page<EventRegistration> {
        return eventRegistrationRepository.findAllByOrganizationId(pageable, organizationId)
    }

    @Transactional(readOnly = true)
    fun getByIdWithEventRoundAndEntryGroup(id: Long): EventRegistration {
        val eventRegistration = getById(id)

        Hibernate.initialize(eventRegistration.eventRound)
        Hibernate.initialize(eventRegistration.entryGroup)
        Hibernate.initialize(eventRegistration.banners)

        return eventRegistration
    }

    @Transactional
    fun registerEvent(eventRegistration: EventRegistration): EventRegistration {
        return eventRegistrationRepository.save(eventRegistration)
    }

    @Transactional
    fun updateEventRegistration(originalEventRegistrationId: Long, updatedEventRegistration: EventRegistration): EventRegistration {
        updatedEventRegistration.updateId(originalEventRegistrationId)
        return eventRegistrationRepository.save(updatedEventRegistration)
    }

    @Transactional
    fun updateStatus(
        id: Long,
        nextStatus: EventRegistrationStatus,
    ): EventRegistration {
        val eventRegistration = getById(id)

        eventRegistration.updateStatus(nextStatus)

        return eventRegistrationRepository.save(eventRegistration)
    }

    @Transactional(readOnly = true)
    fun validateUpdatableStatus(eventRegistration: EventRegistration) {
        if (eventRegistration.status !in listOf(EventRegistrationStatus.검수_진행, EventRegistrationStatus.검수_완료)) {
            throw PublicException(
                publicMessage = "현재 행사 등록 상태가 수정 불가능한 상태입니다.",
                systemMessage = "Not Updatable EventRegistrationStatus : TOKEN =${eventRegistration.status}",
                title = "행사 수정 불가능 상태"
            )
        }
    }
}
