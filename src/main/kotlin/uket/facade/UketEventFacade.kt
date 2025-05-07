package uket.uket.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.uketevent.service.UketEventRoundService
import java.time.LocalDateTime

@Service
class UketEventFacade(
    private val entryGroupService: EntryGroupService,
    private val uketEventRoundService: UketEventRoundService,
) {
    @Transactional(readOnly = true)
    fun findAllValidEntryGroup(eventRoundId: Long, now: LocalDateTime): List<EntryGroup> {
        // validation
        uketEventRoundService.getNowTicketingById(eventRoundId, now)

        val entryGroups = entryGroupService.findAllValidByRoundIdAndStarDateAfter(eventRoundId, now)
        return entryGroups
    }

    @Transactional(readOnly = true)
    fun findValidEntryGroupMap(eventId: Long, at: LocalDateTime): Map<UketEventRound, List<EntryGroup>> {
        val uketEventRounds = uketEventRoundService.getNowTicketingRounds(eventId, at)
        val uketEventRoundIds = uketEventRounds.map { it.id }

        return entryGroupService
            .findValidEntryGroup(uketEventRoundIds, at)
            .groupBy { it.uketEventRound }
    }
}
