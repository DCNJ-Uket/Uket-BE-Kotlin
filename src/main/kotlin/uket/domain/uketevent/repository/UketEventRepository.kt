package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.uketevent.entity.UketEvent
import java.time.LocalDateTime

interface UketEventRepository : JpaRepository<UketEvent, Long> {
    @Query(
        """
        SELECT o.name FROM organization o 
        JOIN uket_event ue ON o.id = ue.organization_id
        WHERE ue.id = :uketEventId
    """,
        nativeQuery = true,
    )
    fun findOrganizationNameByUketEventId(uketEventId: Long): String?

    @Query(
        """
            SELECT ue FROM UketEvent ue 
            WHERE ue.lastRoundDateTime >= CURRENT_DATE
        """
    )
    fun findAllByEventEndDateAfterNowWithUketEventRound(): List<UketEvent>

    @Query(
        """
            SELECT ue from UketEvent ue 
            WHERE ue.id = :uketEventId AND 
            ue.lastRoundDateTime >= CURRENT_DATE
        """
    )
    fun findByIdAndLastRoundDateAfterNowWithBanners(uketEventId: Long): UketEvent?

    fun findAllByOrganizationId(organizationId: Long): List<UketEvent>

    @Query(
        """
            SELECT ue FROM UketEvent ue
            WHERE ue.lastRoundDateTime >= :date
            order by ue.firstRoundDateTime
        """
    )
    fun findAllByLastRoundDateAfterOrderByFirstRoundDateTime(date: LocalDateTime): List<UketEvent>

    @Query(
        """
            SELECT ue FROM UketEvent ue
            WHERE ue.lastRoundDateTime >= :date AND ue.eventType = "FESTIVAL"
            order by ue.firstRoundDateTime
        """
    )
    fun findAllFestivalByLastRoundDateAfterOrderByFirstRoundDateTime(date: LocalDateTime): List<UketEvent>

    @Query(
        """
            SELECT ue FROM UketEvent ue
            WHERE ue.lastRoundDateTime >= :date AND ue.eventType = "PERFORMANCE"
            order by ue.firstRoundDateTime
        """
    )
    fun findAllPerformanceByLastRoundDateAfterOrderByFirstRoundDateTime(date: LocalDateTime): List<UketEvent>
}
