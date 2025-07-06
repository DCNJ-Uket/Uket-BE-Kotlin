package uket.api.user.response

import uket.domain.admin.entity.Organization
import uket.domain.reservation.entity.Ticket
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.user.entity.User
import java.time.LocalDateTime

data class UserTicketResponse(
    val ticketId: Long,
    val userId: Long,
    val entryGroupId: Long,
    val uketEventId: Long,

    val ticketStatus: String,

    val organizationName: String,
    val eventName: String,
    val userName: String,
    val location: String,
    val enterStartDateTime: LocalDateTime,

    val ticketNo: String,
    val reserveAt: LocalDateTime,

    val backgroundImageId: String?,
    val isCancelable: Boolean,
) {
    companion object {
        fun of(
            ticket: Ticket,
            user: User,
            entryGroup: EntryGroup,
            organization: Organization,
            event: UketEvent,
            isCancelable: Boolean,
        ): UserTicketResponse = UserTicketResponse(
            ticketId = ticket.id,
            userId = ticket.userId,
            entryGroupId = ticket.entryGroupId,
            uketEventId = event.id,
            ticketStatus = ticket.status.value,
            organizationName = organization.name,
            eventName = event.eventName,
            userName = user.name,
            location = event.location,
            enterStartDateTime = entryGroup.entryStartDateTime,
            ticketNo = ticket.ticketNo,
            reserveAt = ticket.createdAt,
            backgroundImageId = null,
            isCancelable = isCancelable
        )
    }
}
