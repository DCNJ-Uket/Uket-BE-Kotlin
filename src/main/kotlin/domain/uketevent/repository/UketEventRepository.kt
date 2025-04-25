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
            SELECT ue FROM UketEvent ue 
            WHERE ue.eventEndDateTime >= CURRENT_DATE AND
            ue.eventType = :eventType
        """
    )
    fun findAllByEventTypeAndEventEndDateBeforeNowWithUketEventRound(eventType: EventType): List<UketEvent>

    @Query(
        """
            SELECT ue FROM UketEvent ue 
            WHERE ue.eventEndDateTime >= CURRENT_DATE
        """
    )
    fun findAllByEventEndDateBeforeNowWithUketEventRound(): List<UketEvent>

    @Query("""SELECT ue from UketEvent ue JOIN FETCH Banner b WHERE ue.id = :uketEventId""")
    fun findByIdWithBanners(uketEventId: Long): UketEvent?
}
