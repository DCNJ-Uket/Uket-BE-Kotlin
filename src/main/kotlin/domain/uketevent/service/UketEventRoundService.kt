package uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.repository.UketEventRepository
import uket.domain.uketevent.repository.UketEventRoundRepository

@Service
@Transactional(readOnly = true)
class UketEventRoundService(
    private val uketEventRoundRepository: UketEventRoundRepository,
    private val uketEventRepository: UketEventRepository,
) {
    fun getById(uketEventRoundId: Long): UketEventRound {
        val uketEventRound = uketEventRoundRepository.findByIdOrNull(uketEventRoundId)
            ?: throw IllegalStateException("해당 회차를 찾을 수 없습니다.")
        return uketEventRound
    }

    fun findByUketEventId(uketEventId: Long): List<UketEventRound> =
        uketEventRoundRepository.findByUketEventId(uketEventId, UketEventRound::class.java)

//    fun getNameById(uketEventRoundId: Long): String {
//        val uketEventRound = this.getById(uketEventRoundId)
//        val name = uketEventRound.name
//        return name
//    }
}
