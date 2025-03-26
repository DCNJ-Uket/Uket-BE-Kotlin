package uket.uket.domain.reservation.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import uket.uket.domain.reservation.entity.Ticket
import uket.uket.domain.reservation.enums.TicketStatus
import java.time.LocalDateTime

interface TicketRepository : JpaRepository<Ticket, Long> {
    fun findByStatus(status: TicketStatus, pageable: Pageable): Page<Ticket>

    fun findByCreatedAtBetween(startAt: LocalDateTime, endAt: LocalDateTime, pageable: Pageable): Page<Ticket>

    fun findByUpdatedAtBetween(startAt: LocalDateTime, endAt: LocalDateTime, pageable: Pageable): Page<Ticket>

    fun findByUserIdAndId(userId: Long, ticketId: Long): Ticket?

    fun findAllByUserId(userId: Long): List<Ticket>

    fun findAllByUserIdAndStatusNot(userId: Long, status: TicketStatus): List<Ticket>

//    @Query(
//        """
//            SELECT t FROM Ticket t
//            JOIN EntryGroup e
//            WHERE t.userId = :userId
//            AND t.status NOT IN (:statuses)
//            AND e.entryStartTime >= CURRENT_DATE
//        """,
//    )
//    fun findValidTicketsByUserId(
//        @Param("userId") userId: Long,
//        @Param("statuses") statuses: List<TicketStatus>,
//    ): List<Ticket>

    fun existsByUserIdAndId(userId: Long, ticketId: Long): Boolean

    fun existsByUserIdAndEntryGroupId(userId: Long, entryGroupId: Long): Boolean

    fun existsByUserIdAndEntryGroupIdAndStatusNot(userId: Long, entryGroupId: Long, status: TicketStatus): Boolean

    fun deleteAllByUserId(userId: Long)

//    fun existsByUserIdAndShowAndStatusNot(user: Users?, show: Shows?, status: TicketStatus?): Boolean?

//    @Query("SELECT t FROM Ticket t WHERE t.user.userDetails.depositorName LIKE %:depositorName%")
//    fun findByDepositorName(depositorName: String?, pageable: Pageable?): Page<Ticket?>?

//    @Query("SELECT t FROM Ticket t WHERE t.user.userDetails.phoneNumber LIKE %:lastFourDigits")
//    fun findByPhoneNumberEndingWith(
//        @Param("lastFourDigits") lastFourDigits: String?,
//        pageable: Pageable?
//    ): Page<Ticket?>?

//    @Query("SELECT t FROM Ticket t WHERE t.show.startDate >= :showStart AND t.show.startDate < :showEnd")
//    fun findByShowStartDateBetween(
//        @Param("showStart") showStart: LocalDateTime?,
//        @Param("showEnd") showEnd: LocalDateTime?,
//        pageable: Pageable?
//    ): Page<Ticket?>?

//    fun findByReservationType(userType: ReservationUserType?, pageable: Pageable?): Page<Ticket?>?

//    @Query("SELECT t FROM Ticket t JOIN FETCH t.reservation r WHERE t.user.id = :userId AND t.status <> :status")
//    fun findAllByUserIdAndStatusNotWithReservation(
//        @Param("userId") userId: Long?,
//        @Param("status") status: TicketStatus?
//    ): List<Ticket?>?
}
