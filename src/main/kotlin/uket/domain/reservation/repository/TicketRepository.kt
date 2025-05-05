package uket.domain.reservation.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import uket.api.admin.dto.LiveEnterUserDto
import uket.domain.reservation.dto.TicketSearchDto
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import java.time.LocalDateTime

interface TicketRepository : JpaRepository<Ticket, Long> {
    @Query(
        """
    SELECT new uket.domain.reservation.dto.TicketSearchDto(
        t.id,
        u.depositorName,
        u.phoneNumber,
        ur.eventRoundDateTime,
        t.createdAt,
        t.updatedAt,
        t.status,
        ''
    )
    FROM Ticket t
    JOIN EntryGroup eg ON eg.id = t.entryGroupId
    JOIN eg.uketEventRound ur
    JOIN ur.uketEvent e
    JOIN User u ON u.id = t.userId
    WHERE e.organizationId = :organizationId
      AND (:uketEventId IS NULL OR e.id = :uketEventId)
      AND t.status = :status
"""
    )
    fun findByStatus(
        @Param("organizationId") organizationId: Long,
        @Param("uketEventId") uketEventId: Long?,
        @Param("status") status: TicketStatus,
        pageable: Pageable,
    ): Page<TicketSearchDto>

    @Query(
        """
    SELECT new uket.domain.reservation.dto.TicketSearchDto(
        t.id,
        u.depositorName,
        u.phoneNumber,
        ur.eventRoundDateTime,
        t.createdAt,
        t.updatedAt,
        t.status,
        ''
    )
    FROM Ticket t
    JOIN EntryGroup eg ON eg.id = t.entryGroupId
    JOIN eg.uketEventRound ur
    JOIN ur.uketEvent e
    JOIN User u ON u.id = t.userId
    WHERE e.organizationId = :organizationId
      AND (:uketEventId IS NULL OR e.id = :uketEventId)
      AND t.createdAt BETWEEN :startAt AND :endAt
"""
    )
    fun findByCreatedAtBetween(
        @Param("organizationId") organizationId: Long,
        @Param("uketEventId") uketEventId: Long?,
        @Param("startAt") startAt: LocalDateTime,
        @Param("endAt") endAt: LocalDateTime,
        pageable: Pageable,
    ): Page<TicketSearchDto>

    @Query(
        """
    SELECT new uket.domain.reservation.dto.TicketSearchDto(
        t.id,
        u.depositorName,
        u.phoneNumber,
        ur.eventRoundDateTime,
        t.createdAt,
        t.updatedAt,
        t.status,
        ''
    )
    FROM Ticket t
    JOIN EntryGroup eg ON eg.id = t.entryGroupId
    JOIN eg.uketEventRound ur
    JOIN ur.uketEvent e
    JOIN User u ON u.id = t.userId
    WHERE e.organizationId = :organizationId
      AND (:uketEventId IS NULL OR e.id = :uketEventId)
      AND t.createdAt BETWEEN :startAt AND :endAt
"""
    )
    fun findByUpdatedAtBetween(
        @Param("organizationId") organizationId: Long,
        @Param("uketEventId") uketEventId: Long?,
        @Param("startAt") startAt: LocalDateTime,
        @Param("endAt") endAt: LocalDateTime,
        pageable: Pageable,
    ): Page<TicketSearchDto>

    fun findByUserIdAndId(userId: Long, ticketId: Long): Ticket?

    @Query(
        """
    SELECT new uket.domain.reservation.dto.TicketSearchDto(
        t.id,
        u.depositorName,
        u.phoneNumber,
        ur.eventRoundDateTime,
        t.createdAt,
        t.updatedAt,
        t.status,
        ''
    )
    FROM Ticket t
    JOIN EntryGroup eg ON eg.id = t.entryGroupId
    JOIN eg.uketEventRound ur
    JOIN ur.uketEvent e
    JOIN User u ON u.id = t.userId
    WHERE e.organizationId = :organizationId
      AND (:uketEventId IS NULL OR e.id = :uketEventId)
      AND (u.name LIKE CONCAT('%', :userName, '%'))
"""
    )
    fun findByUserName(
        @Param("organizationId") organizationId: Long,
        @Param("uketEventId") uketEventId: Long?,
        @Param("userName") userName: String,
        pageable: Pageable,
    ): Page<TicketSearchDto>

    @Query(
        """
    SELECT new uket.domain.reservation.dto.TicketSearchDto(
        t.id,
        u.depositorName,
        u.phoneNumber,
        ur.eventRoundDateTime,
        t.createdAt,
        t.updatedAt,
        t.status,
        ''
    )
    FROM Ticket t
    JOIN EntryGroup eg ON eg.id = t.entryGroupId
    JOIN eg.uketEventRound ur
    JOIN ur.uketEvent e
    JOIN User u ON u.id = t.userId
    WHERE e.organizationId = :organizationId
      AND (:uketEventId IS NULL OR e.id = :uketEventId)
      AND ur.eventRoundDateTime BETWEEN :startAt AND :endAt
"""
    )
    fun findByEventRoundTime(
        @Param("organizationId") organizationId: Long,
        @Param("uketEventId") uketEventId: Long?,
        @Param("startAt") startAt: LocalDateTime,
        @Param("endAt") endAt: LocalDateTime,
        pageable: Pageable,
    ): Page<TicketSearchDto>

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

    @Query(
        """
    SELECT new uket.api.admin.dto.LiveEnterUserDto(
        t.enterAt,
        u.depositorName,
        ur.eventRoundDateTime,
        u.phoneNumber,
        t.status
    )
    FROM Ticket t
    JOIN EntryGroup eg ON eg.id = t.entryGroupId
    JOIN eg.uketEventRound ur
    JOIN ur.uketEvent e
    JOIN User u ON u.id = t.userId
    WHERE e.organizationId = :organizationId
    AND (:uketEventId IS NULL OR e.id = :uketEventId)
    AND t.status = :status
"""
    )
    fun findLiveEnterUserDtosByUketEventAndRoundId(
        @Param("organizationId") organizationId: Long,
        @Param("uketEventId") uketEventId: Long?,
        @Param("status") status: TicketStatus,
        pageable: Pageable,
    ): Page<LiveEnterUserDto>

    @Query(
        """
    SELECT new uket.domain.reservation.dto.TicketSearchDto(
        t.id,
        u.depositorName,
        u.phoneNumber,
        ur.eventRoundDateTime,
        t.createdAt,
        t.updatedAt,
        t.status,
        ''
    )
    FROM Ticket t
    JOIN EntryGroup eg ON eg.id = t.entryGroupId
    JOIN eg.uketEventRound ur
    JOIN ur.uketEvent e
    JOIN User u ON u.id = t.userId
    WHERE e.organizationId = :organizationId
    AND (:uketEventId IS NULL OR e.id = :uketEventId)
"""
    )
    fun findAllByOrganizationId(
        @Param("organizationId") organizationId: Long,
        @Param("uketEventId") uketEventId: Long?,
        pageable: Pageable,
    ): Page<TicketSearchDto>

    @Query(
        """
    SELECT new uket.domain.reservation.dto.TicketSearchDto(
        t.id,
        u.depositorName,
        u.phoneNumber,
        ur.eventRoundDateTime,
        t.createdAt,
        t.updatedAt,
        t.status,
        ''
    )
    FROM Ticket t
    JOIN EntryGroup eg ON eg.id = t.entryGroupId
    JOIN eg.uketEventRound ur
    JOIN ur.uketEvent e
    JOIN User u ON u.id = t.userId
    WHERE e.organizationId = :organizationId
      AND (:uketEventId IS NULL OR e.id = :uketEventId)
      AND u.phoneNumber LIKE %:lastFourDigits
"""
    )
    fun findByPhoneNumberEndingWith(
        @Param("organizationId") organizationId: Long,
        @Param("uketEventId") uketEventId: Long?,
        @Param("lastFourDigits") lastFourDigits: String,
        pageable: Pageable,
    ): Page<TicketSearchDto>

    fun existsByUserIdAndId(userId: Long, ticketId: Long): Boolean

    fun existsByUserIdAndEntryGroupId(userId: Long, entryGroupId: Long): Boolean

    fun existsByUserIdAndEntryGroupIdAndStatusNot(userId: Long, entryGroupId: Long, status: TicketStatus): Boolean

    fun deleteAllByUserId(userId: Long)

    @Query(
        """
            SELECT t FROM Ticket t
            JOIN EntryGroup eg ON eg.id = t.entryGroupId
            WHERE t.userId = :userId
            AND eg.uketEventRound.id = :eventRoundId
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
