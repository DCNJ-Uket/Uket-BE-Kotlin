package uket.domain.reservation.service.search

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.api.admin.enums.TicketSearchType
import uket.api.admin.request.SearchRequest
import uket.domain.reservation.dto.TicketSearchDto
import uket.domain.reservation.repository.TicketRepository

@Service
class TicketSearcherByStatus(
    ticketRepository: TicketRepository,
) : TicketSearcher(ticketRepository) {
    override fun isSupport(searchType: TicketSearchType): Boolean {
        return searchType == TicketSearchType.STATUS
    }

    @Transactional(readOnly = true)
    override fun search(
        organizationId: Long,
        uketEventId: Long?,
        searchRequest: SearchRequest,
        pageable: Pageable,
    ): Page<TicketSearchDto> {
        return ticketRepository.findByStatus(organizationId, uketEventId, searchRequest.status!!, pageable)
    }
}
