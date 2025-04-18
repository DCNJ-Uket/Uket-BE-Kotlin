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
            JOIN ue.uketEventRounds uer
            WHERE ue.eventType = :eventType AND
                (   
                    SELECT MAX(uer.eventRoundDateTime)
                    FROM UketEventRound uer
                    WHERE uer.uketEvent = ue
                ) >= CURRENT_TIMESTAMP
        """
    )
    fun findAllByEventTypeAndEventEndDateBeforeNowWithUketEventRound(eventType: EventType): List<UketEvent>

    @Query(
        """
            SELECT ue FROM UketEvent ue 
            JOIN ue.uketEventRounds uer
            WHERE
                (   
                    SELECT MAX(uer.eventRoundDateTime)
                    FROM UketEventRound uer
                    WHERE uer.uketEvent = ue
                ) >= CURRENT_TIMESTAMP
        """
    )
    fun findAllByEventEndDateBeforeNowWithUketEventRound(): List<UketEvent>
}
