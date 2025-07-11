package uket.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.common.enums.EventType
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

        val validEntryGroups = entryGroupService.findValidEntryGroup(uketEventRoundIds, at)

        return uketEventRounds.associateWith { uketEventRound ->
            validEntryGroups.filter { uketEventRound.id == it.uketEventRoundId }
        }
    }

    @Transactional(readOnly = true)
    fun getCurrentEventTicketingStatus(eventId: Long, at: LocalDateTime): TicketingStatus {
        val eventRounds = uketEventRoundService.getEventRoundsByEventId(eventId)
        return getTicketingStatus(eventRounds, at)
    }

    @Transactional(readOnly = true)
    fun getNowActiveEventItemList(type: String, at: LocalDateTime): List<EventListItem> {
        val eventTypes = when (type) {
            "FESTIVAL" -> listOf(EventType.FESTIVAL)
            "PERFORMANCE" -> listOf(EventType.PERFORMANCE)
            else -> listOf(EventType.PERFORMANCE, EventType.PERFORMANCE)
        }
        val activeOrderedEvents = uketEventService.findAllVisibleOrderedEventByEventTypes(eventTypes)

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
