package uket.uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.uket.common.ErrorCode
import uket.uket.domain.uketevent.entity.UketEvent
import uket.uket.domain.uketevent.exception.UketEventException
import uket.uket.domain.uketevent.repository.UketEventRepository

@Service
@Transactional(readOnly = true)
class UketEventService(
    val uketEventRepository: UketEventRepository,
) {
    fun findById(uketEventId: Long): UketEvent {
        val uketEvent = uketEventRepository.findByIdOrNull(uketEventId)
            ?: throw UketEventException(ErrorCode.NOT_FOUND_UKET_EVENT)
        return uketEvent
    }
}
