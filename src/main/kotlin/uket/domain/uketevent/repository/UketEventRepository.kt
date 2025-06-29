package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.common.enums.EventType
import uket.domain.uketevent.entity.UketEvent

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
            WHERE ue.isVisible = true AND ue.eventType IN :eventTypes
            order by ue.firstRoundDateTime
        """
    )
    fun findAllVisibleInEventTypesOrderByFirstRoundDateTime(eventTypes: List<EventType>): List<UketEvent>
}
