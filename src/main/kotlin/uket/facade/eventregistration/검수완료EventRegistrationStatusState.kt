package uket.facade.eventregistration

import org.springframework.stereotype.Component
import uket.common.PublicException
import uket.domain.eventregistration.entity.EventRegistration
import uket.domain.eventregistration.entity.EventRegistrationStatus
import uket.domain.eventregistration.service.EventRegistrationService
import uket.domain.eventregistration.service.EventRegistrationStatusState
import uket.domain.uketevent.service.UketEventService
import uket.facade.CreateUketEventFacade
import uket.facade.DeleteUketEventFacade

@Component
class 검수완료EventRegistrationStatusState(
    val eventRegistrationService: EventRegistrationService,
    val deleteUketEventFacade: DeleteUketEventFacade,
    val createUketEventFacade: CreateUketEventFacade
) : EventRegistrationStatusState {
    override val status: EventRegistrationStatus = EventRegistrationStatus.검수_완료
    override val allowedPrevStatus: Set<EventRegistrationStatus> = setOf(
        EventRegistrationStatus.검수_진행,
        EventRegistrationStatus.등록_완료,
    )

    override fun execute(id: Long, currentStatus: EventRegistrationStatus) {
        when (currentStatus) {
            EventRegistrationStatus.검수_진행 -> 검수진행To검수완료(id)
            EventRegistrationStatus.등록_완료 -> 등록완료To검수완료(id)
            else -> throw PublicException(
                publicMessage = "변경할 수 없는 상태입니다.",
                systemMessage = "[${this::class.simpleName}] ${status}는 현재상태($currentStatus)에서 변경될 수 없는 상태입니다."
            )
        }
    }

    private fun 검수진행To검수완료(id: Long) {
        createUketEventFacade.invoke(id)
    }

    private fun 등록완료To검수완료(id: Long) {
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
