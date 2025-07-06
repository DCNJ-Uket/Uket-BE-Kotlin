package uket.facade

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.reservation.dto.TicketSearchDto
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.user.entity.User
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
        println(entryGroupIds)
        val entryGroupMap = entryGroups.associateBy { it.id }

        val tickets = ticketService.findTicketsByEntryGroupIds(entryGroupIds, pageable)
        val userMap = userService.findByIds(tickets.map { it.userId }.toSet()).associateBy { it.id }

        val resultDtos = tickets.content.mapNotNull { ticket ->
            convertToDto(
                ticket = ticket,
                user = userMap[ticket.userId] ?: return@mapNotNull null,
                entryGroup = entryGroupMap[ticket.entryGroupId] ?: return@mapNotNull null
            )
        }
        return PageImpl(resultDtos, pageable, tickets.totalElements)
    }

    @Transactional(readOnly = true)
    fun toDtoPage(tickets: Page<Ticket>): Page<TicketSearchDto> {
        val userIds = tickets.content.map { it.userId }.toSet()
        val entryGroupIds = tickets.content.map { it.entryGroupId }.toSet()

        val userMap = userService.findByIds(userIds).associateBy { it.id }
        val entryGroupMap = entryGroupService.getByIds(entryGroupIds).associateBy { it.id }
        println(entryGroupIds)

        val dtoList = tickets.content.mapNotNull { ticket ->
            convertToDto(
                ticket = ticket,
                user = userMap[ticket.userId] ?: return@mapNotNull null,
                entryGroup = entryGroupMap[ticket.entryGroupId] ?: return@mapNotNull null
            )
        }
        return PageImpl(dtoList, tickets.pageable, tickets.totalElements)
    }

    private fun convertToDto(
        ticket: Ticket,
        user: User,
        entryGroup: EntryGroup,
    ): TicketSearchDto {
        return TicketSearchDto(
            ticketId = ticket.id,
            depositorName = user.depositorName!!,
            telephone = user.phoneNumber!!,
            showTime = entryGroup.entryStartDateTime,
            orderDate = ticket.createdAt,
            updatedDate = ticket.updatedAt,
            ticketStatus = ticket.status,
            performer = ticket.performerName ?: "",
        )
    }
}
