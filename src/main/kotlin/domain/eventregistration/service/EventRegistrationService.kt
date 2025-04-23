package uket.domain.eventregistration.service

import org.hibernate.Hibernate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.eventregistration.entity.EventRegistration

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
    fun findAll(pageable: Pageable): Page<EventRegistration> {
        return eventRegistrationRepository.findAll(pageable)
    }

    @Transactional(readOnly = true)
    fun getByIdWithEventRoundAndEntryGroup(id: Long): EventRegistration {
        val eventRegistration = getById(id)

        Hibernate.initialize(eventRegistration.eventRound)
        Hibernate.initialize(eventRegistration.entryGroup)

        return eventRegistration
    }

    @Transactional
    fun registerEvent(eventRegistration: EventRegistration): EventRegistration {
        return eventRegistrationRepository.save(eventRegistration)
    }

    @Transactional
    fun updateStatus(
        id: Long,
        registrationStatus: EventRegistrationStatus,
    ): EventRegistration {
        val eventRegistration = getById(id)

        eventRegistration.updateStatus(registrationStatus)

        return eventRegistrationRepository.save(eventRegistration)
    }
}
