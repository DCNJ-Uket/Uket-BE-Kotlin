package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.domain.uketevent.entity.Performer

interface PerformerRepository : JpaRepository<Performer, Long> {
    fun findAllByUketEventRoundId(uketEventRoundId: Long): List<Performer>

    fun findByNameAndUketEventRoundId(name: String, uketEventRoundId: Long): Performer
}
