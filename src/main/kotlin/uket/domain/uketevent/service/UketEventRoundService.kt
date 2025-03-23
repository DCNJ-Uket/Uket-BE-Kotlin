package uket.uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.uket.common.ErrorCode
import uket.uket.domain.uketevent.entity.UketEventRound
import uket.uket.domain.uketevent.exception.UketEventException
import uket.uket.domain.uketevent.repository.UketEventRoundRepository

@Service
@Transactional(readOnly = true)
class UketEventRoundService(
    val uketEventRoundRepository: UketEventRoundRepository,
) {
    fun findById(uketEventRoundId: Long): UketEventRound {
        val uketEventRound = uketEventRoundRepository.findByIdOrNull(uketEventRoundId)
            ?: throw UketEventException(ErrorCode.NOT_FOUND_UKET_EVENT_ROUND)
        return uketEventRound
    }

    fun findByUketEventId(uketEventId: Long): List<UketEventRound> =
        uketEventRoundRepository.findByUketEventId(uketEventId, UketEventRound::class.java)
}
