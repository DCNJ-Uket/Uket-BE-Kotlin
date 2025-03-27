package uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.repository.UketEventRepository

@Service
@Transactional(readOnly = true)
class UketEventService(
    private val uketEventRepository: UketEventRepository,
) {
    fun findById(uketEventId: Long): UketEvent {
        val uketEvent = uketEventRepository.findByIdOrNull(uketEventId)
            ?: throw IllegalStateException("해당 행사를 찾을 수 없습니다.")
        return uketEvent
    }
}
