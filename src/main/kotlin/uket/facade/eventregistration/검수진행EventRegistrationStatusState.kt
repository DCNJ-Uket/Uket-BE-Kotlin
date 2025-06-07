package uket.facade.eventregistration

import org.springframework.stereotype.Component
import uket.common.PublicException
import uket.domain.eventregistration.entity.EventRegistration
import uket.domain.eventregistration.entity.EventRegistrationStatus
import uket.domain.eventregistration.service.EventRegistrationService
import uket.domain.eventregistration.service.EventRegistrationStatusState
import uket.facade.DeleteUketEventFacade

@Component
class 검수진행EventRegistrationStatusState(
    val eventRegistrationService: EventRegistrationService,
    val deleteUketEventFacade: DeleteUketEventFacade
) : EventRegistrationStatusState {
    override val status: EventRegistrationStatus = EventRegistrationStatus.검수_진행
    override val allowedPrevStatus: Set<EventRegistrationStatus> = setOf(EventRegistrationStatus.검수_완료)

    override fun execute(id: Long, currentStatus: EventRegistrationStatus) {
        when (currentStatus) {
            EventRegistrationStatus.검수_완료 -> 검수완료To검수진행(id)
            else -> throw PublicException(
                publicMessage = "변경할 수 없는 상태입니다.",
                systemMessage = "[${this::class.simpleName}] ${status}는 현재상태($currentStatus)에서 변경될 수 없는 상태입니다."
            )
        }
    }

    private fun 검수완료To검수진행(id: Long) {
        val eventRegistration = eventRegistrationService.findById(id)
        val uketEventId = eventRegistration?.uketEventId

        if (uketEventId != null) {
            deleteUketEventFacade.invoke(uketEventId)
        }
    }

    override fun updateStatus(id: Long): EventRegistration = eventRegistrationService.updateStatus(id, status)

    companion object {
        val doNothing = {}
    }
}
