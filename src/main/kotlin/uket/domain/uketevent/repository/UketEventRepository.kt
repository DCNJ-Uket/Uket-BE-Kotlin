package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
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
}
