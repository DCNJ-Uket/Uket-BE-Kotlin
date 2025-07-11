package uket.domain.reservation.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import java.time.LocalDateTime

interface TicketRepository : JpaRepository<Ticket, Long> {
    @Query(
        """
    SELECT t FROM Ticket t
    WHERE t.status = :status
    AND t.entryGroupId IN :entryGroupIds
"""
    )
    fun findByStatus(
        @Param("status") status: TicketStatus,
        @Param("entryGroupIds") entryGroupIds: Collection<Long>,
        pageable: Pageable,
    ): Page<Ticket>

    @Query(
        """
        SELECT t FROM Ticket t
        WHERE t.createdAt BETWEEN :startAt AND :endAt
        AND t.entryGroupId IN :entryGroupIds
    """
    )
    fun findByCreatedAtBetweenInEntryGroupIds(
        @Param("startAt") startAt: LocalDateTime,
        @Param("endAt") endAt: LocalDateTime,
        @Param("entryGroupIds") entryGroupIds: Collection<Long>,
        pageable: Pageable,
    ): Page<Ticket>

    @Query(
        """
    SELECT t FROM Ticket t
    WHERE t.updatedAt BETWEEN :startAt AND :endAt
    AND t.entryGroupId IN :entryGroupIds
"""
    )
    fun findByUpdatedAtBetween(
        @Param("startAt") startAt: LocalDateTime,
        @Param("endAt") endAt: LocalDateTime,
        @Param("entryGroupIds") entryGroupIds: Collection<Long>,
        pageable: Pageable,
    ): Page<Ticket>

    fun findByUserIdAndId(userId: Long, ticketId: Long): Ticket?

    @Query(
        """
    SELECT t FROM Ticket t
    JOIN User u ON u.id = t.userId
    WHERE (u.name LIKE CONCAT('%', :userName, '%'))
    AND t.entryGroupId IN :entryGroupIds
"""
    )
    fun findByUserName(
        @Param("userName") userName: String,
        @Param("entryGroupIds") entryGroupIds: Collection<Long>,
        pageable: Pageable,
    ): Page<Ticket>

    @Query(
        """
        SELECT t FROM Ticket t
        WHERE t.entryGroupId IN :entryGroupIds
"""
    )
    fun findByEventRoundTime(
        @Param("entryGroupIds") entryGroupIds: Collection<Long>,
        pageable: Pageable,
    ): Page<Ticket>

    fun findAllByUserId(userId: Long): List<Ticket>

    fun findTicketsByEntryGroupIdIn(
        entryGroupIds: Set<Long>,
        pageable: Pageable,
    ): Page<Ticket>

    fun findTicketsByEntryGroupIdInAndStatus(
        entryGroupIds: Set<Long>,
        status: TicketStatus,
        pageable: Pageable,
    ): Page<Ticket>

    @Query(
        """
    SELECT t FROM Ticket t
    JOIN User u ON u.id = t.userId
    WHERE u.phoneNumber LIKE %:lastFourDigits
    AND t.entryGroupId IN :entryGroupIds
"""
    )
    fun findByPhoneNumberEndingWith(
        @Param("lastFourDigits") lastFourDigits: String,
        @Param("entryGroupIds") entryGroupIds: Collection<Long>,
        pageable: Pageable,
    ): Page<Ticket>

    fun existsByUserIdAndId(userId: Long, ticketId: Long): Boolean

    fun existsByUserIdAndEntryGroupId(userId: Long, entryGroupId: Long): Boolean

    fun existsByUserIdAndEntryGroupIdAndStatusNot(userId: Long, entryGroupId: Long, status: TicketStatus): Boolean

    fun deleteAllByUserId(userId: Long)

    @Query(
        """
            SELECT t FROM Ticket t
            JOIN EntryGroup eg ON eg.id = t.entryGroupId
            WHERE t.userId = :userId
            AND eg.uketEventRoundId = :eventRoundId
            AND t.status NOT IN :status
        """
    )
    fun findAllbyUserIdAndEventRoundIdAndStatusNot(userId: Long, eventRoundId: Long, status: List<TicketStatus>): List<Ticket>

    @Query(
        """
            SELECT t FROM Ticket t
            WHERE t.ticketNo IN :ticketNos
        """
    )
    fun findByTicketNoIn(ticketNos: List<String>): List<Ticket>
}
