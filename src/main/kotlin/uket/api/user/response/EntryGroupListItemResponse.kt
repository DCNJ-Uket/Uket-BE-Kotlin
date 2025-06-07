package uket.api.user.response

import uket.domain.uketevent.entity.EntryGroup
import java.time.LocalDateTime

data class EntryGroupListItemResponse(
    val entryGroupId: Long,
    val startDateTime: LocalDateTime,
    val ticketCount: Int,
    val totalTicketCount: Int,
) {
    companion object {
        fun of(entryGroup: EntryGroup): EntryGroupListItemResponse = EntryGroupListItemResponse(
            entryGroupId = entryGroup.id,
            startDateTime = entryGroup.entryStartDateTime,
            ticketCount = entryGroup.ticketCount,
            totalTicketCount = entryGroup.totalTicketCount
        )
    }
}
