package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.domain.uketevent.entity.EntryGroup

interface EntryGroupRepository : JpaRepository<EntryGroup, Long> {
    fun <T> findByUketEventRoundId(uketEventRoundId: Long, type: Class<T>): List<T>
}
