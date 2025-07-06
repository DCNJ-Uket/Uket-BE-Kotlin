package uket.facade

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.reservation.dto.TicketSearchDto
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.user.service.UserService

@Service
@Transactional(readOnly = true)
class TicketEntryGroupUserFacade(
    val ticketService: TicketService,
    val entryGroupService: EntryGroupService,
    val userService: UserService,
) {
//    @Transactional
//    fun deleteAllUserTickets(userId: Long) {
//        val tickets: List<Ticket> = ticketService.findAllByUserIdAndStatusNotWithEntryGroup(userId, TicketStatus.RESERVATION_CANCEL)
//        for (ticket in tickets) {
//            entryGroupService.decreaseReservedCount(ticket.entryGroupId)
//        }
//        ticketService.deleteAllByUserId(userId)
//    }

    @Transactional
    fun updateTicketStatus(ticketId: Long, ticketStatus: TicketStatus) {
        val ticket = ticketService.getById(ticketId)

        if (ticketStatus === TicketStatus.RESERVATION_CANCEL) {
            entryGroupService.decreaseReservedCount(ticket.entryGroupId)
        }

        if (ticketStatus === TicketStatus.FINISH_ENTER) {
            ticket.enter()
            ticketService.updateTicket(ticket)
            return
        }

        val updatedTicket = ticket.updateStatus(ticketStatus)
        ticketService.updateTicket(updatedTicket)
    }

    @Transactional(readOnly = true)
    fun searchAllTickets(organizationId: Long, uketEventId: Long?, pageable: Pageable): Page<TicketSearchDto> {
        val entryGroups = entryGroupService.getEntryGroups(organizationId, uketEventId)
        val entryGroupIds = entryGroups.map { it.id }.toSet()
        val entryGroupMap = entryGroups.associateBy { it.id }

        val tickets = ticketService.findTicketsByEntryGroupIds(entryGroupIds, pageable)

        val userMap = userService.findByIds(tickets.map { it.userId }.toSet()).associateBy { it.id }

        val resultDtos = tickets.map { ticket ->
            val entryGroup = entryGroupMap[ticket.entryGroupId]
            val user = userMap[ticket.userId]

            TicketSearchDto(
                ticketId = ticket.id,
                depositorName = user!!.depositorName!!,
                telephone = user.phoneNumber!!,
                showTime = entryGroup!!.entryStartDateTime,
                orderDate = ticket.createdAt,
                updatedDate = ticket.updatedAt,
                ticketStatus = ticket.status,
                performer = ticket.performerName,
            )
        }

        return resultDtos
    }
}
