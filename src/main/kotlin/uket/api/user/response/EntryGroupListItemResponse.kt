package uket.uket.api.user.response

import uket.domain.uketevent.entity.EntryGroup
import java.time.LocalDateTime

data class EntryGroupListItemResponse(
    val entryGroupId: Long,
    val name: String,
    val startDateTime: LocalDateTime,
    val ticketCount: Int,
    val totalTicketCount: Int,
) {
    companion object {
        fun of(entryGroup: EntryGroup): EntryGroupListItemResponse = EntryGroupListItemResponse(
            entryGroupId = entryGroup.id,
            name = entryGroup.entryGroupName,
            startDateTime = entryGroup.entryStartDateTime,
            ticketCount = entryGroup.ticketCount,
            totalTicketCount = entryGroup.totalTicketCount
        )
    }
}
