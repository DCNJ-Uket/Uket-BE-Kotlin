package uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.repository.EntryGroupRepository
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
        val entryGroups = entryGroupRepository.findByUketEventRoundIdAndStartDateAfter(
            uketEventRoundId,
            date.truncatedTo(ChronoUnit.DAYS)
        )
        return entryGroups.filter { it.ticketCount < it.totalTicketCount }
    }

    @Transactional
    fun increaseReservedCount(entryGroupId: Long, count: Int) {
        val entryGroup = this.getById(entryGroupId)
        entryGroup.increaseReservedCount(count)
        entryGroupRepository.save(entryGroup)
    }

    @Transactional
    fun decreaseReservedCount(entryGroupId: Long) {
        val entryGroup = this.getById(entryGroupId)
        val isSuccess: Boolean = entryGroup.decreaseReservedCount()

        check(isSuccess) {
            "예매된 티켓이 존재하지 않습니다."
        }
        entryGroupRepository.save(entryGroup)
    }

    @Transactional(readOnly = true)
    fun findValidEntryGroup(uketEventRoundIds: List<Long>, at: LocalDateTime): List<EntryGroup> =
        entryGroupRepository.findByUketEventIdAndStartDateTimeAfterWithUketEventRound(uketEventRoundIds, at)

    @Transactional
    fun saveAll(entryGroups: List<EntryGroup>): List<EntryGroup> {
        return entryGroupRepository.saveAll(entryGroups)
    }

    @Transactional
    fun deleteAllByEventId(uketEventId: Long) {
        val entryGroups = entryGroupRepository.findAllByUketEventId(uketEventId)

        entryGroupRepository.deleteAllInBatch(entryGroups)
    }
}
