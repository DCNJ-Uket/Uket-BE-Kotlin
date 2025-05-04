package uket.uket.facade

import org.springframework.stereotype.Service
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.service.EntryGroupService
import java.time.LocalDateTime

@Service
class UketEventFacade(
    private val entryGroupService: EntryGroupService,
) {
    fun findValidEntryGroupMap(eventId: Long, at: LocalDateTime): Map<UketEventRound, List<EntryGroup>> = entryGroupService
        .findValidEntryGroup(eventId, at)
        .groupBy { it.uketEventRound }
}
