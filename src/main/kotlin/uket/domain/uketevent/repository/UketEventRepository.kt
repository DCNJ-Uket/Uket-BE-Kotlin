package uket.uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.uket.domain.uketevent.entity.UketEvent

interface UketEventRepository : JpaRepository<UketEvent, Long>
