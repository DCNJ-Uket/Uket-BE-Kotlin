package uket.uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.uket.domain.uketevent.entity.UketEventRound

interface UketEventRoundRepository : JpaRepository<UketEventRound, Long> {
    fun <T> findByUketEventId(uketEventId: Long, type: Class<T>): List<T>
}
