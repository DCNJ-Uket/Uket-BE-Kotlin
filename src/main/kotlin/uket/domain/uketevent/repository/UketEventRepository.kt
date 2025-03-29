package uket.domain.uketevent.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.domain.uketevent.entity.UketEvent
import uket.uket.domain.uketevent.repository.UketEventRepositoryCustom

interface UketEventRepository :
    JpaRepository<UketEvent, Long>,
    UketEventRepositoryCustom
