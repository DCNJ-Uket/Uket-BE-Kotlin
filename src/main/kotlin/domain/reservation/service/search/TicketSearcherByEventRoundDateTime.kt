package uket.domain.reservation.service.search

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.api.admin.enums.TicketSearchType
import uket.api.admin.request.SearchRequest
import uket.domain.reservation.dto.TicketSearchDto
import uket.domain.reservation.repository.TicketRepository
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class TicketSearcherByEventRoundDateTime(
    ticketRepository: TicketRepository,
) : TicketSearcher(ticketRepository) {
    override fun isSupport(searchType: TicketSearchType): Boolean {
        return searchType == TicketSearchType.SHOW_DATE
    }

    @Transactional(readOnly = true)
    override fun search(
        organizationId: Long,
        uketEventId: Long?,
        searchRequest: SearchRequest,
        pageable: Pageable,
    ): Page<TicketSearchDto> {
        val showDate = searchRequest.showDate
            ?: throw IllegalStateException("showDate가 null일 수 없습니다.")

        val showStart: LocalDateTime = showDate.atStartOfDay()
        val showEnd: LocalDateTime = showDate.atTime(LocalTime.MAX)

        return ticketRepository.findByEventRoundTime(organizationId, uketEventId,showStart, showEnd, pageable);
    }
}
