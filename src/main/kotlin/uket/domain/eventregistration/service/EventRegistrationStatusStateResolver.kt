package uket.uket.domain.eventregistration.service

import org.springframework.stereotype.Component
import uket.domain.eventregistration.entity.EventRegistrationStatus

@Component
class EventRegistrationStatusStateResolver(
    val eventRegistrationStatusStates: List<EventRegistrationStatusState>,
) {
    fun resolve(eventRegistrationStatus: EventRegistrationStatus): EventRegistrationStatusState {
        return eventRegistrationStatusStates.find {
            it.isSupport(eventRegistrationStatus)
        } ?: throw IllegalStateException("[EventRegistrationStatusStateResolver] 아직 구현되지 않은 상태입니다. | eventRegistrationStatus: $eventRegistrationStatus")
    }
}
