package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.uketevent.entity.UketEventRound

interface UketEventRoundRepository : JpaRepository<UketEventRound, Long> {
    @Query(
        """
            SELECT uer FROM UketEventRound uer
            JOIN uer.uketEvent ue ON ue.id = :uketEventId
            WHERE uer.eventRoundDateTime >= CURRENT_DATE
        """
    )
    fun findByUketEventIdAAndEventRoundDateAfterNow(uketEventId: Long): List<UketEventRound>
}
