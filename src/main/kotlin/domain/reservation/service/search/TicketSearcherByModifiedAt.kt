package uket.domain.reservation.service.search

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.api.admin.enums.TicketSearchType
import uket.api.admin.request.SearchRequest
import uket.domain.reservation.dto.TicketSearchDto
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.repository.TicketRepository
import java.time.LocalTime

@Service
class TicketSearcherByModifiedAt(
    ticketRepository: TicketRepository
) : TicketSearcher(ticketRepository) {

    override fun isSupport(searchType: TicketSearchType): Boolean {
        return searchType == TicketSearchType.MODIFIED_AT
    }

    @Transactional(readOnly = true)
    override fun search(uketEventId: Long, searchRequest: SearchRequest, pageable: Pageable): Page<TicketSearchDto> {
        val modifiedAt = searchRequest.modifiedAt
            ?: throw IllegalStateException("modifiedAt은 null일 수 없습니다.")

        val modifyStart = modifiedAt.toLocalDate().atTime(LocalTime.MIN)
        val modifyEnd = modifiedAt.toLocalDate().atTime(LocalTime.MAX)

        return ticketRepository.findByUpdatedAtBetween(uketEventId, modifyStart, modifyEnd, pageable)
    }
}
