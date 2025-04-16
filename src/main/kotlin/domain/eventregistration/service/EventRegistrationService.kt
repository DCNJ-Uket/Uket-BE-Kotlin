package uket.domain.eventregistration.service

import org.hibernate.Hibernate
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
}
