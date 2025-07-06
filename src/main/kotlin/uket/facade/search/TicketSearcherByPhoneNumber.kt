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

@Service
class TicketSearcherByPhoneNumber(
    ticketRepository: TicketRepository,
    entryGroupService: EntryGroupService,
) : TicketSearcher(ticketRepository, entryGroupService) {
    override fun isSupport(searchType: TicketSearchType): Boolean {
        return searchType == TicketSearchType.PHONE_NUMBER
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

        return ticketRepository.findByPhoneNumberEndingWith(searchRequest.phoneNumberLastFourDigits!!, entryGroupIds, pageable)
    }
}
