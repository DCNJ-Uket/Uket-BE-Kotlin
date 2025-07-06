package uket.domain.reservation.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.api.admin.dto.LiveEnterUserDto
import uket.domain.reservation.dto.CreateTicketCommand
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.repository.TicketRepository
import uket.uket.domain.reservation.repository.TicketJdbcRepository
import uket.uket.modules.push.UserMessageTemplate
import java.util.UUID

@Service
@Transactional(readOnly = true)
class TicketService(
    private val ticketRepository: TicketRepository,
    private val ticketJdbcRepository: TicketJdbcRepository,
) {
    fun getById(ticketId: Long): Ticket {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw IllegalStateException("해당 티켓을 찾을 수 없습니다")
        return ticket
    }

    fun findLiveEnterTickets(organizationId: Long, uketEventId: Long?, pageable: Pageable): Page<LiveEnterUserDto> =
        ticketRepository.findLiveEnterUserDtosByUketEventAndRoundId(
            organizationId,
            uketEventId,
            TicketStatus.FINISH_ENTER,
            pageable
        )

    fun findTicketsByEntryGroupIds(entryGroupIds: Set<Long>, pageable: Pageable): Page<Ticket> =
        ticketRepository.findTicketsByEntryGroupIdIn(entryGroupIds, pageable)

    fun findTicketsByEntryGroupIdsAndStatus(entryGroupIds: Set<Long>, status: TicketStatus, pageable: Pageable): Page<Ticket> =
        ticketRepository.findTicketsByEntryGroupIdInAndStatus(entryGroupIds, status, pageable)

    @Transactional
    fun publishTickets(createTicketCommand: CreateTicketCommand, count: Int): List<Ticket> {
        println(createTicketCommand.performerName)
        val tickets = List(count) {
            Ticket(
                userId = createTicketCommand.userId,
                entryGroupId = createTicketCommand.entryGroupId,
                status = createTicketCommand.ticketStatus,
                ticketNo = createTicketNo(),
                performerName = createTicketCommand.performerName,
                enterAt = null,
            )
        }
        ticketJdbcRepository.saveAllBatch(tickets)

        val ticketNos = tickets.map { it.ticketNo }
        return ticketRepository.findByTicketNoIn(ticketNos)
    }

    private fun createTicketNo() = UUID
        .randomUUID()
        .toString()
        .replace("-", "")
        .substring(0, UserMessageTemplate.TEMPLATE_MAXIMUM_LENGTH)

    @Transactional
    fun updateTicket(ticket: Ticket) {
        ticketRepository.save(ticket)
    }

    @Transactional
    fun updateTicketStatus(ticketId: Long, ticketStatus: TicketStatus): Ticket {
        val ticket: Ticket = ticketRepository.findById(ticketId).get()
        if (ticketStatus == TicketStatus.FINISH_ENTER) ticket.enter() else ticket.updateStatus(ticketStatus)
        return ticketRepository.save(ticket)
    }

    @Transactional
    fun deleteAllByUserId(userId: Long) {
        ticketRepository.deleteAllByUserId(userId)
    }

    @Transactional(readOnly = true)
    fun findAllByUserId(userId: Long): List<Ticket> = ticketRepository.findAllByUserId(userId)

    fun findAllActiveByUserAndEventRound(userId: Long, entryGroupId: Long): List<Ticket> =
        ticketRepository.findAllbyUserIdAndEventRoundIdAndStatusNot(
            userId,
            entryGroupId,
            TicketStatus.notActiveStatuses
        )

    fun checkTicketOwner(userId: Long, ticketId: Long) {
        val ticket = this.getById(ticketId)
        check(ticket.userId == userId) {
            throw IllegalArgumentException("유저가 소유한 티켓이 아닙니다.")
        }
    }

    fun checkCancelTicketStatus(ticketId: Long) {
        val ticket = getById(ticketId)
        check(ticket.status in TicketStatus.cancelableStatuses) {
            throw IllegalStateException("티켓 취소 가능한 상태가 아닙니다.")
        }
    }
}
