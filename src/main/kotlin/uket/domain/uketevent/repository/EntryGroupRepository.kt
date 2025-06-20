package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.uketevent.entity.EntryGroup
import java.time.LocalDateTime

interface EntryGroupRepository : JpaRepository<EntryGroup, Long> {
    fun findAllByUketEventId(uketEventId: Long): List<EntryGroup>

    @Query(
        """
            SELECT eg FROM EntryGroup eg
            WHERE eg.entryStartDateTime >= :date
            AND eg.uketEventRoundId = :uketEventRoundId
        """
    )
    fun findByUketEventRoundIdAndStartDateAfter(uketEventRoundId: Long, date: LocalDateTime): List<EntryGroup>

    @Query(
        """
            SELECT eg FROM EntryGroup eg
            WHERE eg.uketEventRoundId IN :uketEventRoundIds
            AND eg.entryStartDateTime >= :at
        """
    )
    fun findByUketEventIdAndStartDateTimeAfterWithUketEventRound(uketEventRoundIds: List<Long>, at: LocalDateTime): List<EntryGroup>
}
