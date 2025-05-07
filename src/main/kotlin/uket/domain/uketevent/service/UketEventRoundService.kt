package uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.common.PublicException
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.repository.UketEventRoundRepository
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class UketEventRoundService(
    private val uketEventRoundRepository: UketEventRoundRepository,
) {
    @Transactional(readOnly = true)
    fun getById(uketEventRoundId: Long): UketEventRound {
        val uketEventRound = uketEventRoundRepository.findByIdOrNull(uketEventRoundId)
            ?: throw IllegalStateException("해당 회차를 찾을 수 없습니다.")
        return uketEventRound
    }

    @Transactional(readOnly = true)
    fun getNowTicketingById(uketEventRoundId: Long, at: LocalDateTime): UketEventRound {
        val uketEventRound = this.getById(uketEventRoundId)
        if (!uketEventRound.isNowTicketing(at)) {
            throw PublicException(
                publicMessage = "현재 티켓팅이 불가능한 회차입니다.",
                systemMessage = "[UketEventRoundService] 티켓팅이 불가능한 회차 조회 UketEventRound${uketEventRound.id}",
                title = "예매 불가능 한 회차"
            )
        }
        return uketEventRound
    }

    @Transactional(readOnly = true)
    fun getNowTicketingRounds(uketEventId: Long, at: LocalDateTime): List<UketEventRound> {
        val nowTicketingRounds = uketEventRoundRepository
            .findByUketEventIdAndEventRoundDateAfter(uketEventId, at.truncatedTo(ChronoUnit.DAYS))
            .filter { it.isNowTicketing(at) }

        validateTicketingRounds(nowTicketingRounds, uketEventId)

        return nowTicketingRounds
    }

    private fun validateTicketingRounds(
        nowTicketingRounds: List<UketEventRound>,
        uketEventId: Long,
    ) {
        if (nowTicketingRounds.isEmpty()) {
            throw PublicException(
                publicMessage = "현재 티켓팅이 불가능한 행사입니다",
                systemMessage = "[UketEventRoundService] 티켓팅이 진행 중인 회차가 존재하지 않는 행사의 회차 목록 조회 UketEvent$uketEventId",
                title = "예매 불가능 한 행사"
            )
        }
    }

    @Transactional(readOnly = true)
    fun getEventRoundsMapByActiveEventIds(activeEventIds: List<Long>): Map<Long, List<UketEventRound>> {
        val rounds = uketEventRoundRepository.findAllByUketEventIdInWithUketEvent(activeEventIds)
        return rounds.groupBy { it.uketEvent!!.id }
    }
}
