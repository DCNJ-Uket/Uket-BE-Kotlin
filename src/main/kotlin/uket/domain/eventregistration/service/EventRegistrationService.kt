package uket.uket.domain.eventregistration.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.eventregistration.entity.EventRegistration
import uket.domain.eventregistration.service.EventRegistrationRepository

@Service
class EventRegistrationService(
    val eventRegistrationRepository: EventRegistrationRepository,
) {
    @Transactional
    fun registerEvent(eventRegistration: EventRegistration): EventRegistration {
        return eventRegistrationRepository.save(eventRegistration)
    }
}
