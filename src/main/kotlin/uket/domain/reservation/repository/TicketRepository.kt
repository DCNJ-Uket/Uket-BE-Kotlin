package uket.uket.domain.reservation.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import uket.uket.domain.reservation.entity.Ticket
import uket.uket.domain.reservation.enums.TicketStatus
import java.time.LocalDateTime

interface TicketRepository :
    JpaRepository<Ticket, Long>,
    TicketRepositoryCustom {
    fun findByStatus(status: TicketStatus, pageable: Pageable): Page<Ticket>

    fun findByCreatedAtBetween(startAt: LocalDateTime, endAt: LocalDateTime, pageable: Pageable): Page<Ticket>

    fun findByUpdatedAtBetween(startAt: LocalDateTime, endAt: LocalDateTime, pageable: Pageable): Page<Ticket>

    fun findByUserIdAndId(userId: Long, ticketId: Long): Ticket?

    fun findAllByUserId(userId: Long): List<Ticket>

    fun findAllByUserIdAndStatusNot(userId: Long, status: TicketStatus): List<Ticket>

    fun existsByUserIdAndId(userId: Long, ticketId: Long): Boolean

    fun existsByUserIdAndEntryGroupId(userId: Long, entryGroupId: Long): Boolean

    fun existsByUserIdAndEntryGroupIdAndStatusNot(userId: Long, entryGroupId: Long, status: TicketStatus): Boolean

    fun deleteAllByUserId(userId: Long)
}
