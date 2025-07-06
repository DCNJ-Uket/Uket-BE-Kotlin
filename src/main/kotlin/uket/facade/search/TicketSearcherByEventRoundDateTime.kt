package uket.facade.search

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.api.admin.enums.TicketSearchType
import uket.api.admin.request.SearchRequest
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.repository.TicketRepository
import uket.domain.uketevent.service.EntryGroupService
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class TicketSearcherByEventRoundDateTime(
    ticketRepository: TicketRepository,
    entryGroupService: EntryGroupService,
) : TicketSearcher(ticketRepository,entryGroupService) {
    override fun isSupport(searchType: TicketSearchType): Boolean {
        return searchType == TicketSearchType.SHOW_DATE
    }

    @Transactional(readOnly = true)
    override fun search(
        organizationId: Long,
        uketEventId: Long?,
        searchRequest: SearchRequest,
        pageable: Pageable,
    ): Page<Ticket> {
        val showDate = searchRequest.showDate
            ?: throw IllegalStateException("showDate가 null일 수 없습니다.")
        val showStart: LocalDateTime = showDate.atStartOfDay()
        val showEnd: LocalDateTime = showDate.atTime(LocalTime.MAX)

        val entryGroupIds = entryGroupService.getEntryGroups(
            organizationId = organizationId,
            uketEventId = uketEventId
        ).filter {
            it.entryStartDateTime.isAfter(showStart) && it.entryStartDateTime.isBefore(showEnd)
        }.map { it.id }.toSet()

        return ticketRepository.findByEventRoundTime(entryGroupIds, pageable);
    }
}
