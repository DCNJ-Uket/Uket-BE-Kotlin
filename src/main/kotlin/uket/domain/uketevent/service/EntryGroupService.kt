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
class EntryGroupService(
    private val entryGroupRepository: EntryGroupRepository,
) {
    @Transactional(readOnly = true)
    fun getById(entryGroupId: Long): EntryGroup {
        val entryGroup = entryGroupRepository.findByIdOrNull(entryGroupId)
            ?: throw IllegalStateException("해당 입장 그룹을 찾을 수 없습니다.")
        return entryGroup
    }

    @Transactional(readOnly = true)
    fun findAllValidByRoundIdAndStarDateAfter(uketEventRoundId: Long, date: LocalDateTime): List<EntryGroup> {
        val entryGroups = entryGroupRepository.findByUketEventRoundIdAndStartDateAfter(uketEventRoundId, date.truncatedTo(ChronoUnit.DAYS))
        return entryGroups.filter { it.ticketCount < it.totalTicketCount }
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
    fun findValidEntryGroup(uketEventRoundIds: List<Long>, at: LocalDateTime): List<EntryGroup> =
        entryGroupRepository.findByUketEventIdAndStartDateTimeAfterWithUketEventRound(uketEventRoundIds, at)
}
