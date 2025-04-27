package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.uketevent.entity.UketEventRound
import java.time.LocalDateTime

interface UketEventRoundRepository : JpaRepository<UketEventRound, Long> {
    @Query(
        """
            SELECT uer FROM UketEventRound uer
            JOIN uer.uketEvent ue ON ue.id = :uketEventId
            WHERE uer.eventRoundDateTime >= :date
        """
    )
    fun findByUketEventIdAAndEventRoundDateAfter(uketEventId: Long, date: LocalDateTime): List<UketEventRound>

    @Query(
        """
            SELECT uer FROM UketEventRound uer
            JOIN FETCH uer.uketEvent ue
            WHERE uer.id = :uketEventRoundId
        """
    )
    fun findByIdWithUketEvent(uketEventRoundId: Long): UketEventRound?
}
