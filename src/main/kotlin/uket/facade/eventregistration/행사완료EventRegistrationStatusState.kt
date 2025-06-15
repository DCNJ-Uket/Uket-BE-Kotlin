package uket.facade.eventregistration

import org.springframework.stereotype.Component
import uket.common.PublicException
import uket.domain.eventregistration.entity.EventRegistration
import uket.domain.eventregistration.entity.EventRegistrationStatus
import uket.domain.eventregistration.service.EventRegistrationService
import uket.domain.eventregistration.service.EventRegistrationStatusState
import uket.domain.uketevent.service.UketEventService

@Component
class 행사완료EventRegistrationStatusState(
    val eventRegistrationService: EventRegistrationService,
    val uketEventService: UketEventService,
) : EventRegistrationStatusState {
    override val status: EventRegistrationStatus = EventRegistrationStatus.행사_완료
    override val allowedPrevStatus: Set<EventRegistrationStatus> = setOf(EventRegistrationStatus.등록_완료)

    override fun execute(id: Long, currentStatus: EventRegistrationStatus) {
        when (currentStatus) {
            EventRegistrationStatus.등록_완료 -> 등록완료To행사완료(id)
            else -> throw PublicException(
                publicMessage = "변경할 수 없는 상태입니다.",
                systemMessage = "[${this::class.simpleName}] ${status}는 현재상태($currentStatus)에서 변경될 수 없는 상태입니다."
            )
        }
    }

    private fun 등록완료To행사완료(id: Long) {
        val eventRegistration = eventRegistrationService.getById(id)
        uketEventService.finish(eventRegistration.uketEventId!!)
    }

    override fun updateStatus(id: Long): EventRegistration = eventRegistrationService.updateStatus(id, status)
}
