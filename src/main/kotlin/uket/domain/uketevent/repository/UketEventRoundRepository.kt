package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.uketevent.entity.UketEventRound
import java.time.LocalDateTime

interface UketEventRoundRepository : JpaRepository<UketEventRound, Long> {
    @Query(
        """
            SELECT uer FROM UketEventRound uer
            WHERE uer.eventRoundDateTime >= :date
            AND uer.uketEventId = :uketEventId
        """
    )
    fun findByUketEventIdAndEventRoundDateAfter(uketEventId: Long, date: LocalDateTime): List<UketEventRound>

    @Query(
        """
            SELECT uer FROM UketEventRound uer
            WHERE uer.uketEventId IN :eventIds
        """
    )
    fun findAllByUketEventIdInWithUketEvent(eventIds: List<Long>): List<UketEventRound>
}
