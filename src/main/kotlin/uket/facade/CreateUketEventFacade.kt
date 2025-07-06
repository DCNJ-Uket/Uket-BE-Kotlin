package uket.facade

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uket.domain.eventregistration.entity.EventRegistrationStatus
import uket.domain.eventregistration.service.EventRegistrationService
import uket.domain.payment.service.PaymentService
import uket.domain.uketevent.service.BannerService
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.uketevent.service.UketEventRoundService
import uket.domain.uketevent.service.UketEventService

@Component
class CreateUketEventFacade(
    private val uketEventService: UketEventService,
    private val uketEventRoundService: UketEventRoundService,
    private val entryGroupService: EntryGroupService,
    private val bannerService: BannerService,
    private val eventRegistrationService: EventRegistrationService,
    private val paymentService: PaymentService,
) {
    @Transactional
    fun invoke(eventRegistrationId: Long) {
        val eventRegistration = eventRegistrationService.getByIdWithEventRoundAndEntryGroup(eventRegistrationId)
        check(eventRegistration.status == EventRegistrationStatus.검수_진행) {
            "[CreateUketEventFacade] 검수 진행 상태가 아닌데 UketEvent를 생성할 수 없습니다. | eventRegistrationId: ${eventRegistration.id}"
        }

        val savedPayment = paymentService.save(eventRegistration.toPayment())
        var savedEventRegistration = eventRegistrationService.settingPayment(
            eventRegistrationId = eventRegistrationId,
            paymentId = savedPayment.id
        )

        val savedUketEvent = uketEventService.save(savedEventRegistration.toUketEvent())
        savedEventRegistration = eventRegistrationService.settingEvent(
            eventRegistrationId = eventRegistrationId,
            uketEventId = savedUketEvent.id
        )

        val eventRounds = uketEventRoundService.saveAll(savedEventRegistration.toEventRounds())
        eventRounds.map {
            entryGroupService.saveAll(savedEventRegistration.toEntryGroups(it))
        }
        bannerService.saveAll(savedEventRegistration.toBanners())
    }
}
