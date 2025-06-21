package uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.uketevent.entity.Performer
import uket.domain.uketevent.repository.PerformerRepository

@Service
class PerformerService(
    val performerRepository: PerformerRepository,
) {
    @Transactional(readOnly = true)
    fun getById(id: Long): Performer {
        val performer = performerRepository.findByIdOrNull(id)
            ?: throw IllegalStateException("참여자를 찾을 수 없습니다.")
        return performer
    }

    @Transactional
    fun addTicketCountForPerformer(performerId: Long, ticketCount: Int) {
        val performer = getById(performerId)
        performer.addTicketCount(ticketCount)
    }

    @Transactional
    fun minusTicketCountForPerformer(performerId: Long, ticketCount: Int) {
        val performer = getById(performerId)
        performer.minusTicketCount(ticketCount)
    }

    @Transactional(readOnly = true)
    fun findAllByUketEventRoundId(roundId: Long): List<Performer> = performerRepository.findAllByUketEventRoundId(roundId)

    fun findByNameAndRoundId(name: String, roundId: Long): Performer = performerRepository.findByNameAndUketEventRoundId(name, roundId)
}
