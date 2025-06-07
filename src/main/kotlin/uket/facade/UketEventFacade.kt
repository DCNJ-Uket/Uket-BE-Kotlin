package uket.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.uketevent.dto.EventListItem
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.enums.TicketingStatus
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.uketevent.service.UketEventRoundService
import uket.domain.uketevent.service.UketEventService
import java.time.LocalDateTime

@Service
class UketEventFacade(
    private val entryGroupService: EntryGroupService,
    private val uketEventRoundService: UketEventRoundService,
    private val uketEventService: UketEventService,
) {
    @Transactional(readOnly = true)
    fun findAllValidEntryGroup(eventRoundId: Long, now: LocalDateTime): List<EntryGroup> {
        // validation
        uketEventRoundService.getNowTicketingById(eventRoundId, now)

        val entryGroups = entryGroupService.findAllValidByRoundIdAndStarDateAfter(eventRoundId, now)
        return entryGroups
    }

    @Transactional(readOnly = true)
    fun getValidEntryGroupMap(eventId: Long, at: LocalDateTime): Map<UketEventRound, List<EntryGroup>> {
        val uketEventRounds = uketEventRoundService.getNowTicketingRounds(eventId, at)
        val uketEventRoundIds = uketEventRounds.map { it.id }

        return entryGroupService
            .findValidEntryGroup(uketEventRoundIds, at)
            .groupBy { it.uketEventRound }
    }

    @Transactional(readOnly = true)
    fun getNowActiveEventItemList(type: String, at: LocalDateTime): List<EventListItem> {
        val activeOrderedEvents = when (type) {
            "FESTIVAL" -> uketEventService.findAllNowActiveOrderedFestival(at)
            "PERFORMANCE" -> uketEventService.findAllNowActiveOrderedPerformance(at)
            else -> uketEventService.findAllNowActiveOrdered(at)
        }

        val activeEventIds = activeOrderedEvents.map { it.id }
        val roundsMap = uketEventRoundService.getEventRoundsMapByEventIds(activeEventIds)

        val eventItemList = activeOrderedEvents.map {
            val rounds = roundsMap[it.id]!!
            val status = getTicketingStatus(rounds, at)
            EventListItem.of(it, status, rounds)
        }

        val orderedList = eventItemList
            .sortedWith(
                ticketingStatusComparator()
                    .thenBy {
                        it.eventStartDate
                    }
            )

        return orderedList
    }

    private fun ticketingStatusComparator() = compareBy<EventListItem> {
        when (it.ticketingStatus) {
            TicketingStatus.티켓팅_진행중 -> 1
            TicketingStatus.오픈_예정 -> 2
            TicketingStatus.티켓팅_종료 -> 3
            else -> 4
        }
    }

    private fun getTicketingStatus(uketEventRounds: List<UketEventRound>, at: LocalDateTime): TicketingStatus = when {
        uketEventRounds.any { it.isNowTicketing(at) } -> TicketingStatus.티켓팅_진행중
        uketEventRounds.all { it.isTicketingEnd(at) } -> TicketingStatus.티켓팅_종료
        else -> TicketingStatus.오픈_예정
    }
}
