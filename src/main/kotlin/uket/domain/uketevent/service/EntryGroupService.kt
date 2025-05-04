package uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.repository.EntryGroupRepository
import uket.modules.redis.aop.DistributedLock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
@Transactional(readOnly = true)
class EntryGroupService(
    private val entryGroupRepository: EntryGroupRepository,
) {
    fun getById(entryGroupId: Long): EntryGroup {
        val entryGroup = entryGroupRepository.findByIdOrNull(entryGroupId)
            ?: throw IllegalStateException("해당 입장 그룹을 찾을 수 없습니다.")
        return entryGroup
    }

    fun findValidByUketEventRoundIdAfter(uketEventRoundId: Long, at: LocalDateTime): List<EntryGroup> {
        val entryGroups = entryGroupRepository.findByUketEventRoundIdAfter(uketEventRoundId, at)
        return entryGroups.mapNotNull { if (it.ticketCount >= it.totalTicketCount) null else it }
    }

    @Transactional
    fun increaseReservedCount(entryGroupId: Long) {
        val entryGroup = this.getById(entryGroupId)
        val isSuccess: Boolean = entryGroup.increaseReservedCount()

        if (java.lang.Boolean.FALSE == isSuccess) {
            throw IllegalStateException("해당 입장 그룹의 예매 가능 인원이 없습니다.")
        }
        entryGroupRepository.save(entryGroup)
    }

    @DistributedLock(key = "#reservationId")
    fun decreaseReservedCount(entryGroupId: Long) {
        val entryGroup = this.getById(entryGroupId)
        val isSuccess: Boolean = entryGroup.decreaseReservedCount()

        check(isSuccess) {
            "예매된 티켓이 존재하지 않습니다."
        }
    }

    @Transactional(readOnly = true)
    fun findValidEntryGroup(eventId: Long, at: LocalDateTime): List<EntryGroup> = entryGroupRepository.findByUketEventIdAndAfterWithUketEventRound(
        eventId,
        at.truncatedTo(ChronoUnit.DAYS),
        at
    )
}
