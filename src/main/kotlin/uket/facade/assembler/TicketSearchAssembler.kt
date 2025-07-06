package uket.facade.assembler

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Component
import uket.domain.reservation.dto.TicketSearchDto
import uket.domain.reservation.entity.Ticket
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.user.service.UserService

@Component
class TicketSearchAssembler(
    private val userService: UserService,
    private val entryGroupService: EntryGroupService,
) {
    fun toDtoPage(tickets: Page<Ticket>): Page<TicketSearchDto> {
        val userIds = tickets.content.map { it.userId }.toSet()
        val entryGroupIds = tickets.content.map { it.entryGroupId }.toSet()

        val userMap = userService.findByIds(userIds).associateBy { it.id }
        val entryGroupMap = entryGroupService.getByIds(entryGroupIds).associateBy { it.id }

        val dtoList = tickets.content.map { ticket ->
            val user = userMap[ticket.userId]
            val entryGroup = entryGroupMap[ticket.entryGroupId]

            TicketSearchDto(
                ticketId = ticket.id,
                depositorName = user!!.depositorName!!,
                telephone = user.phoneNumber!!,
                showTime = entryGroup!!.entryStartDateTime,
                orderDate = ticket.createdAt,
                updatedDate = ticket.updatedAt,
                ticketStatus = ticket.status,
                performer = ticket.performerName ?: "",
            )
        }

        return PageImpl(dtoList, tickets.pageable, tickets.totalElements)
    }
}
