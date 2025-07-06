package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import uket.domain.uketevent.entity.EntryGroup
import java.time.LocalDateTime

interface EntryGroupRepository : JpaRepository<EntryGroup, Long> {
    fun findAllByUketEventId(uketEventId: Long): List<EntryGroup>

    @Query(
        """
    SELECT eg FROM EntryGroup eg
    JOIN UketEvent ue ON eg.uketEventId = ue.id
    WHERE ue.organizationId = :organizationId
"""
    )
    fun findIdsByOrganizationId(
        @Param("organizationId") organizationId: Long,
    ): List<EntryGroup>

    @Query(
        """
    SELECT eg d .fake except FROM EntryGroup eg
    JOIN UketEvent ue ON eg.uketEventId = ue.id
    WHERE ue.organizationId = :organizationId AND ue.id = :uketEventId
"""
    )
    fun findIdsByOrganizationIdAndEventId(
        @Param("organizationId") organizationId: Long,
        @Param("uketEventId") uketEventId: Long,
    ): List<EntryGroup>

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
