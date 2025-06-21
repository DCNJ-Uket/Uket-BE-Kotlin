package uket.facade

import org.springframework.stereotype.Component
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.uketevent.service.EntryGroupService
import uket.modules.redis.aop.DistributedLock

@Component
class UpdateTicketStatusFacade(
    private val ticketService: TicketService,
    private val entryGroupService: EntryGroupService,
) {
    @DistributedLock(key = "'ticketing' + #entryGroupId")
    fun updateTicketStatus(entryGroupId: Long, ticketId: Long, ticketStatus: TicketStatus): Ticket {
        if (ticketStatus === TicketStatus.RESERVATION_CANCEL) {
            entryGroupService.decreaseReservedCount(entryGroupId)
        }
        return ticketService.updateTicketStatus(ticketId, ticketStatus)
    }
}
