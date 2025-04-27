package uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.repository.UketEventRoundRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
@Transactional(readOnly = true)
class UketEventRoundService(
    private val uketEventRoundRepository: UketEventRoundRepository,
) {
    fun getById(uketEventRoundId: Long): UketEventRound {
        val uketEventRound = uketEventRoundRepository.findByIdOrNull(uketEventRoundId)
            ?: throw IllegalStateException("해당 회차를 찾을 수 없습니다.")
        return uketEventRound
    }

    fun getByIdWithUketEvent(uketEventRoundId: Long): UketEventRound {
        val uketEventRound = uketEventRoundRepository.findByIdWithUketEvent(uketEventRoundId)
            ?: throw IllegalStateException("해당 회차를 찾을 수 없습니다.")
        return uketEventRound
    }

    fun findByUketEventIdAndDateAfter(uketEventId: Long, date: LocalDateTime): List<UketEventRound> =
        uketEventRoundRepository.findByUketEventIdAAndEventRoundDateAfter(uketEventId, date.truncatedTo(ChronoUnit.DAYS))
}
