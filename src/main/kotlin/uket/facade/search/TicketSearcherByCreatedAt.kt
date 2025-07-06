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
import java.time.LocalTime

@Service
class TicketSearcherByCreatedAt(
    ticketRepository: TicketRepository,
    entryGroupService: EntryGroupService,
) : TicketSearcher(ticketRepository,entryGroupService) {
    override fun isSupport(searchType: TicketSearchType): Boolean {
        return searchType == TicketSearchType.CREATED_AT
    }

    @Transactional(readOnly = true)
    override fun search(
        organizationId: Long,
        uketEventId: Long?,
        searchRequest: SearchRequest,
        pageable: Pageable,
    ): Page<Ticket> {
        val entryGroupIds = entryGroupService.getEntryGroups(
            organizationId = organizationId,
            uketEventId = uketEventId
        ).map { it.id }.toSet()

        val createdAt = searchRequest.createdAt
            ?: throw IllegalStateException("createdAt은 null일 수 없습니다.")

        val createStart = createdAt.toLocalDate().atTime(LocalTime.MIN)
        val createEnd = createdAt.toLocalDate().atTime(LocalTime.MAX)

        return ticketRepository.findByCreatedAtBetweenInEntryGroupIds(createStart, createEnd,entryGroupIds, pageable)
    }
}
