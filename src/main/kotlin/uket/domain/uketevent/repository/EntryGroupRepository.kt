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
}
