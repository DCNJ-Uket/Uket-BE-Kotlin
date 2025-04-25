package uket.domain.reservation.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import java.time.LocalDateTime

interface TicketRepository : JpaRepository<Ticket, Long> {
    fun findByStatus(status: TicketStatus, pageable: Pageable): Page<Ticket>

    fun findByCreatedAtBetween(startAt: LocalDateTime, endAt: LocalDateTime, pageable: Pageable): Page<Ticket>

    fun findByUpdatedAtBetween(startAt: LocalDateTime, endAt: LocalDateTime, pageable: Pageable): Page<Ticket>

    fun findByUserIdAndId(userId: Long, ticketId: Long): Ticket?

    @Query(
        """
        SELECT t FROM Ticket t
        WHERE t.userId = :userId
        AND t.status NOT IN :status
        AND t.entryGroupId IN (
            SELECT eg.id FROM EntryGroup eg 
            WHERE eg.uketEventRound.uketEvent.lastRoundDateTime >= CURRENT_DATE
        )
    """,
    )
    fun findValidTicketsByUserIdAndStatusNotIn(userId: Long, excludedStatuses: List<TicketStatus>): List<Ticket>

    fun findAllByUserId(userId: Long): List<Ticket>

    fun findAllByUserIdAndStatusNot(userId: Long, status: TicketStatus): List<Ticket>

    @Query(
        """
            SELECT t.* 
            FROM ticket t 
            JOIN entry_group eg ON t.entry_group_id = eg.id 
            WHERE t.user_id = :userId 
              AND t.status <> :status
        """,
        nativeQuery = true,
    )
    fun findAllByUserIdAndStatusNotWithEntryGroup(userId: Long, status: TicketStatus): List<Ticket>

    fun existsByUserIdAndId(userId: Long, ticketId: Long): Boolean

    fun existsByUserIdAndEntryGroupId(userId: Long, entryGroupId: Long): Boolean

    fun existsByUserIdAndEntryGroupIdAndStatusNot(userId: Long, entryGroupId: Long, status: TicketStatus): Boolean

    fun deleteAllByUserId(userId: Long)
}
