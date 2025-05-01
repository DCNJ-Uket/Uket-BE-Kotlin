package uket.uket.facade.eventregistration

import org.springframework.stereotype.Component
import uket.domain.eventregistration.entity.EventRegistration
import uket.domain.eventregistration.entity.EventRegistrationStatus
import uket.domain.eventregistration.service.EventRegistrationService
import uket.uket.domain.eventregistration.service.EventRegistrationStatusState

@Component
class 행사완료EventRegistrationStatusState(
    val eventRegistrationService: EventRegistrationService,
) : EventRegistrationStatusState {
    override val status: EventRegistrationStatus = EventRegistrationStatus.행사_완료
    override val allowedPrevStatus: Set<EventRegistrationStatus> = setOf(EventRegistrationStatus.등록_완료)

    override fun execute(id: Long) {
        return
    }

    override fun updateStatus(id: Long): EventRegistration {
        return eventRegistrationService.updateStatus(id, status)
    }
}
