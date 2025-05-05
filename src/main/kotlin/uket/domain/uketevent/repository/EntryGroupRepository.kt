package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.uketevent.entity.EntryGroup
import java.time.LocalDateTime

interface EntryGroupRepository : JpaRepository<EntryGroup, Long> {
    @Query(
        """
            SELECT eg FROM EntryGroup eg
            WHERE eg.entryStartDateTime >= :at
        """
    )
    fun findByUketEventRoundIdAfter(uketEventRoundId: Long, at: LocalDateTime): List<EntryGroup>

    @Query(
        """
            SELECT eg FROM EntryGroup eg
            JOIN FETCH eg.uketEventRound uer
            WHERE uer.uketEvent.id = :eventId
            AND eg.entryStartDateTime >= :timeAt
            AND uer.eventRoundDateTime >= :dateAt
            AND eg.ticketCount < eg.totalTicketCount
        """
    )
    fun findByUketEventIdAndAfterWithUketEventRound(eventId: Long, dateAt: LocalDateTime, timeAt: LocalDateTime): List<EntryGroup>

    @Query(
        """
        SELECT eg FROM EntryGroup eg
        JOIN FETCH eg.uketEventRound uer
        JOIN FETCH uer.uketEvent
        WHERE eg.id = :entryGroupId
    """
    )
    fun findByIdWithUketEventRoundAndUketEvent(entryGroupId: Long): EntryGroup?
}
