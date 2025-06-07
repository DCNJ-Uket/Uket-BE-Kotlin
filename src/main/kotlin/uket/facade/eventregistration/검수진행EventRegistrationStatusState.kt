package uket.facade.eventregistration

import org.springframework.stereotype.Component
import uket.domain.eventregistration.entity.EventRegistration
import uket.domain.eventregistration.entity.EventRegistrationStatus
import uket.domain.eventregistration.service.EventRegistrationService
import uket.domain.eventregistration.service.EventRegistrationStatusState

@Component
class 검수진행EventRegistrationStatusState(
    val eventRegistrationService: EventRegistrationService,
) : EventRegistrationStatusState {
    override val status: EventRegistrationStatus = EventRegistrationStatus.검수_진행
    override val allowedPrevStatus: Set<EventRegistrationStatus> = setOf()

    override fun execute(id: Long, currentStatus: EventRegistrationStatus) {
        return
    }

    override fun updateStatus(id: Long): EventRegistration = eventRegistrationService.updateStatus(id, status)
}
