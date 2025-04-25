package uket.domain.reservation.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.api.admin.dto.LiveEnterUserDto
import uket.domain.reservation.dto.CreateTicketCommand
import uket.domain.reservation.dto.TicketSearchDto
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.repository.TicketRepository
import java.util.UUID

@Service
@Transactional(readOnly = true)
class TicketService(
    private val ticketRepository: TicketRepository,
) {
    fun getById(ticketId: Long): Ticket {
        val ticket = ticketRepository.findByIdOrNull(ticketId)
            ?: throw IllegalStateException("해당 티켓을 찾을 수 없습니다")
        return ticket
    }

    fun findAllByUserIdAndStatusNotWithEntryGroup(
        userId: Long,
        ticketStatus: TicketStatus,
    ): List<Ticket> = ticketRepository.findAllByUserIdAndStatusNotWithEntryGroup(userId, ticketStatus)

    fun findAllTicketsByUserId(userId: Long): List<Ticket> {
        val excludedStatuses: List<TicketStatus> = listOf(TicketStatus.RESERVATION_CANCEL, TicketStatus.EXPIRED)
        return ticketRepository.findValidTicketsByUserIdAndStatusNotIn(userId, excludedStatuses)
    }

    fun findLiveEnterTickets(organizationId: Long, uketEventId: Long?, pageable: Pageable): Page<LiveEnterUserDto> {
        return ticketRepository.findLiveEnterUserDtosByUketEventAndRoundId(organizationId, uketEventId, TicketStatus.FINISH_ENTER, pageable)
    }

    fun searchAllTickets(organizationId: Long, pageable: Pageable): Page<TicketSearchDto> {
        return ticketRepository.findAllByOrganizationId(organizationId, pageable)
    }

    @Transactional
    fun publishTicket(createTicketCommand: CreateTicketCommand): Ticket {
        val ticket: Ticket = Ticket(
            userId = createTicketCommand.userId,
            entryGroupId = createTicketCommand.entryGroupId,
            status = createTicketCommand.ticketStatus,
            ticketNo = UUID.randomUUID().toString(),
            enterAt = null,
        )

        return ticketRepository.save(ticket)
    }

    @Transactional
    fun updateTicket(ticket: Ticket) {
        ticketRepository.save(ticket)
    }

    @Transactional
    fun updateTicketStatus(ticketId: Long, ticketStatus: TicketStatus): Ticket {
        val ticket: Ticket = ticketRepository.findById(ticketId).get()
        ticket.updateStatus(ticketStatus)
        return ticketRepository.save(ticket)
    }

    @Transactional
    fun cancelTicketByUserIdAndId(userId: Long, ticketId: Long) {
        val ticket: Ticket = ticketRepository.findByUserIdAndId(userId, ticketId)
            ?: throw IllegalStateException("해당 티켓을 찾을 수 없습니다.")

        ticket.cancel()
        ticket.updateDeletedAt()
        ticketRepository.save(ticket)
    }

    @Transactional
    fun deleteAllByUserId(userId: Long) {
        ticketRepository.deleteAllByUserId(userId)
    }

    fun validateTicketStatus(ticketId: Long) {
        val ticket = this.getById(ticketId)

        val ticketStatus: TicketStatus = ticket.status

        if (ticketStatus === TicketStatus.FINISH_ENTER) {
            throw IllegalStateException("입장이 이미 완료된 티켓입니다. 재입장은 담당자에게 문의 부탁드립니다.")
        } else if (ticketStatus === TicketStatus.EXPIRED) {
            throw IllegalStateException("이미 기간이 지난 티켓입니다. 날짜를 확인해주세요.")
        }
    }

    fun checkTicketOwner(userId: Long, ticketId: Long) {
        if (java.lang.Boolean.FALSE == ticketRepository.existsByUserIdAndId(userId, ticketId)) {
            throw IllegalStateException("해당 사용자는 해당 티켓을 소유하고 있지 않습니다.")
        }
    }
}
