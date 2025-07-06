package uket.facade.search

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import uket.api.admin.enums.TicketSearchType
import uket.api.admin.request.SearchRequest
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.repository.TicketRepository
import uket.domain.uketevent.service.EntryGroupService

interface SearchTicket {
    fun search(organizationId: Long, uketEventId: Long?, searchRequest: SearchRequest, pageable: Pageable): Page<Ticket>
}

abstract class TicketSearcher(
    protected val ticketRepository: TicketRepository,
    protected val entryGroupService: EntryGroupService,
) : SearchTicket {
    abstract fun isSupport(searchType: TicketSearchType): Boolean
}
