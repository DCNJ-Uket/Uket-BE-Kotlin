package uket.uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.uket.common.ErrorCode
import uket.uket.domain.uketevent.entity.EntryGroup
import uket.uket.domain.uketevent.exception.UketEventException
import uket.uket.domain.uketevent.repository.EntryGroupRepository

@Service
@Transactional(readOnly = true)
class EntryGroupService(
    val entryGroupRepository: EntryGroupRepository,
) {
    fun findById(entryGroupId: Long): EntryGroup {
        val entryGroup = entryGroupRepository.findByIdOrNull(entryGroupId)
            ?: throw UketEventException(ErrorCode.NOT_FOUND_ENTRY_GROUP)
        return entryGroup
    }

    fun findByUketEventRound(uketEventRoundId: Long): List<EntryGroup> =
        entryGroupRepository.findByUketEventRoundId(uketEventRoundId, EntryGroup::class.java)

    @Transactional
    fun increaseReservedCount(entryGroupId: Long) {
        val entryGroup = this.findById(entryGroupId)
        val isSuccess: Boolean = entryGroup.increaseReservedCount()

        if (java.lang.Boolean.FALSE == isSuccess) {
            throw UketEventException(ErrorCode.FAIL_RESERVATION_COUNT)
        }
        entryGroupRepository.save(entryGroup)
    }
}
