package uket.domain.reservation.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import java.time.LocalDateTime

interface TicketRepositoryCustom {
    fun findValidTicketsByUserId(
        userId: Long,
        statuses: List<TicketStatus>,
    ): List<Ticket>

    fun findByDepositorName(depositorName: String, pageable: Pageable): Page<Ticket>

    fun findByPhoneNumberEndingWith(
        lastFourDigits: String,
        pageable: Pageable,
    ): Page<Ticket>

    fun findByUketEventRoundEventDateBetween(
        uketEventRoundStartDate: LocalDateTime,
        uketEventRoundEndDate: LocalDateTime,
        pageable: Pageable,
    ): Page<Ticket>

    fun findAllByUserIdAndStatusNotWithEntryGroup(
        userId: Long,
        status: TicketStatus,
    ): List<Ticket>
}
