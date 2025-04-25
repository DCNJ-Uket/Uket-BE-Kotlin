package uket.domain.reservation.service.search

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import uket.api.admin.enums.TicketSearchType
import uket.api.admin.request.SearchRequest
import uket.domain.reservation.dto.TicketSearchDto
import uket.domain.reservation.repository.TicketRepository

interface SearchTicket {
    fun search(organizationId: Long, uketEventId: Long, searchRequest: SearchRequest, pageable: Pageable): Page<TicketSearchDto>
}

abstract class TicketSearcher(
    protected val ticketRepository: TicketRepository,
) : SearchTicket {
    abstract fun isSupport(searchType: TicketSearchType): Boolean
}
