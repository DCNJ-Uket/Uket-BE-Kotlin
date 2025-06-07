package uket.facade

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uket.domain.eventregistration.service.EventRegistrationService
import uket.domain.uketevent.service.BannerService
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.uketevent.service.UketEventRoundService
import uket.domain.uketevent.service.UketEventService

@Component
class DeleteUketEventFacade(
    private val uketEventService: UketEventService,
    private val uketEventRoundService: UketEventRoundService,
    private val entryGroupService: EntryGroupService,
    private val bannerService: BannerService,
    private val eventRegistrationService: EventRegistrationService,
) {
    @Transactional
    fun invoke(uketEventId: Long) {
        bannerService.deleteAllByEventId(uketEventId)
        entryGroupService.deleteAllByEventId(uketEventId)
        uketEventRoundService.deleteAllByEventId(uketEventId)
        uketEventService.deleteById(uketEventId)
        eventRegistrationService.clearUketEvent(uketEventId)
    }
}
