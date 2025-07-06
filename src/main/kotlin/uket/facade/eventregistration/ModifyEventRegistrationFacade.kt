package uket.facade.eventregistration

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uket.domain.eventregistration.entity.EventRegistration
import uket.domain.eventregistration.entity.EventRegistrationStatus
import uket.domain.eventregistration.service.EventRegistrationService
import uket.domain.eventregistration.service.EventRegistrationStatusStateResolver

@Component
class ModifyEventRegistrationFacade(
    private val eventRegistrationService: EventRegistrationService,
    private val eventRegistrationStatusStateResolver: EventRegistrationStatusStateResolver,
) {
    @Transactional
    fun modify(
        originalEventRegistrationId: Long,
        eventRegistration: EventRegistration,
    ): Long {
        val updatedEventRegistration = eventRegistrationService.updateEventRegistration(
            originalEventRegistrationId, eventRegistration
        )

        val eventRegistrationStatusState = eventRegistrationStatusStateResolver.resolve(EventRegistrationStatus.검수_진행)
        val eventRegistration = eventRegistrationStatusState.invoke(
            id = originalEventRegistrationId,
            currentStatus = updatedEventRegistration.status
        )

        return eventRegistration.id
    }
}
