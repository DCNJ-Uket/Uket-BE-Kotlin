package uket.facade

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.api.admin.response.EnterUketEventResponse
import uket.api.admin.response.LiveEnterUserResponse
import uket.auth.filter.TokenValidator
import uket.auth.jwt.JwtTicketUtil
import uket.auth.jwt.JwtValues.JWT_PAYLOAD_VALUE_TICKET
import uket.common.PublicException
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.user.service.UserService
import uket.facade.assembler.LiveEnterUserAssembler

@Service
class EnterUketEventFacade(
    private val tokenValidator: TokenValidator,
    private val jwtTicketUtil: JwtTicketUtil,
    private val ticketService: TicketService,
    private val userService: UserService,
    private val entryGroupService: EntryGroupService,
    private val liveEnterUserAssembler: LiveEnterUserAssembler,
) {
    @Transactional
    fun enterUketEvent(ticketToken: String): EnterUketEventResponse {
        tokenValidator.validateExpiredToken(ticketToken)
        tokenValidator.validateTokenCategory(JWT_PAYLOAD_VALUE_TICKET, ticketToken)
        tokenValidator.validateTokenSignature(ticketToken)

        val ticketId = jwtTicketUtil.getTicketId(ticketToken)
        val ticket = ticketService.getById(ticketId)
        val user = userService.getById(ticket.userId)

        validateBeforePaymentTicket(ticket.status)
        validateAlreadyEnterTicket(ticket.status)

        ticket.enter()

        return EnterUketEventResponse.of(ticket, user)
    }

    @Transactional(readOnly = true)
    fun searchLiveEnterUsers(organizationId: Long, uketEventId: Long?, pageable: Pageable): Page<LiveEnterUserResponse> {
        val entryGroups = entryGroupService.getEntryGroups(organizationId, uketEventId)
        val entryGroupIds = entryGroups.map { it.id }.toSet()
        val entryGroupMap = entryGroups.associateBy { it.id }

        val tickets = ticketService.findTicketsByEntryGroupIdsAndStatus(entryGroupIds, TicketStatus.FINISH_ENTER, pageable)

        val userMap = userService.findByIds(tickets.map { it.userId }.toSet()).associateBy { it.id }

        return tickets.map { ticket ->
            liveEnterUserAssembler.assemble(
                ticket = ticket,
                user = userMap[ticket.userId]!!,
                entryGroup = entryGroupMap[ticket.entryGroupId]!!
            )
        }
    }

    private fun validateBeforePaymentTicket(status: TicketStatus) {
        if (status == TicketStatus.BEFORE_PAYMENT) {
            throw PublicException(
                publicMessage = "예약은 완료되었으나 아직 해당 티켓 금액이 입금되지 않았습니다.",
                systemMessage = "Not Valid TicketStatus For Enter : TICKETSTATUS =$status",
                title = "입금되지 않은 티켓"
            )
        }
    }

    private fun validateAlreadyEnterTicket(status: TicketStatus) {
        if (status == TicketStatus.FINISH_ENTER) {
            throw PublicException(
                publicMessage = "입장이 이미 완료된 티켓입니다. 재입장은 담당자에게 문의 부탁드립니다.",
                systemMessage = "Not Valid TicketStatus For Enter : TICKETSTATUS =$status",
                title = "입장이 완료된 티켓"
            )
        }
    }
}
