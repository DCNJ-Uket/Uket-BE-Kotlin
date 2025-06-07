package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.uketevent.entity.EntryGroup
import java.time.LocalDateTime

interface EntryGroupRepository : JpaRepository<EntryGroup, Long> {
    @Query(
        """
            SELECT eg FROM EntryGroup eg
            WHERE eg.entryStartDateTime >= :date
            AND eg.uketEventRound.id = :uketEventRoundId
        """
    )
    fun findByUketEventRoundIdAndStartDateAfter(uketEventRoundId: Long, date: LocalDateTime): List<EntryGroup>

    @Query(
        """
            SELECT eg FROM EntryGroup eg
            JOIN FETCH eg.uketEventRound uer
            WHERE uer.id IN :uketEventRoundIds
            AND eg.entryStartDateTime >= :at
        """
    )
    fun findByUketEventIdAndStartDateTimeAfterWithUketEventRound(uketEventRoundIds: List<Long>, at: LocalDateTime): List<EntryGroup>

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
