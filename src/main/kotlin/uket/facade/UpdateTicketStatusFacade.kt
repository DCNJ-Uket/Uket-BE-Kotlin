package uket.facade

import org.springframework.stereotype.Component
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.uketevent.service.UketEventService
import uket.domain.user.service.UserService
import uket.modules.redis.aop.DistributedLock
import uket.uket.facade.TicketingCompletionMessageSendService

@Component
class UpdateTicketStatusFacade(
    private val ticketService: TicketService,
    private val entryGroupService: EntryGroupService,
    private val ticketingCompletionMessageSendService: TicketingCompletionMessageSendService,
    private val userService: UserService,
    private val uketEventService: UketEventService,
) {
    @DistributedLock(key = "'ticketing' + #entryGroupId")
    fun updateTicketStatus(entryGroupId: Long, ticketId: Long, ticketStatus: TicketStatus, userId: Long): Ticket {
        if (ticketStatus === TicketStatus.RESERVATION_CANCEL) {
            entryGroupService.decreaseReservedCount(entryGroupId)
        }
        if (ticketStatus == TicketStatus.BEFORE_ENTER) {
            val user = userService.getById(userId)
            val entryGroup = entryGroupService.getById(entryGroupId)
            val event = uketEventService.getById(entryGroup.uketEventId)
            val ticket = ticketService.getById(ticketId)

            ticketingCompletionMessageSendService.send(
                userName = user.name,
                userPhoneNumber = user.phoneNumber!!,
                eventName = event.eventName,
                eventType = event.eventType,
                ticketNo = ticket.ticketNo,
                eventDate = entryGroup.entryStartDateTime.toString(),
                eventLocation = event.location
            )
        }
        return ticketService.updateTicketStatus(ticketId, ticketStatus)
    }
}
