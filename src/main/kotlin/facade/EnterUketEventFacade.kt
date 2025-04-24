package uket.facade

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.api.admin.dto.LiveEnterUserDto
import uket.api.admin.response.EnterUketEventResponse
import uket.api.admin.response.LiveEnterUserResponse
import uket.auth.filter.TokenValidator
import uket.auth.jwt.JwtTicketUtil
import uket.auth.jwt.JwtValues.JWT_PAYLOAD_VALUE_TICKET
import uket.common.PublicException
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.user.entity.User
import uket.domain.user.service.UserService

@Service
class EnterUketEventFacade(
    private val tokenValidator: TokenValidator,
    private val jwtTicketUtil: JwtTicketUtil,
    private val ticketService: TicketService,
    private val userService: UserService,
) {
    @Transactional
    fun enterUketEvent(ticketToken: String): EnterUketEventResponse {
        tokenValidator.validateExpiredToken(ticketToken)
        tokenValidator.validateTokenCategory(JWT_PAYLOAD_VALUE_TICKET, ticketToken)
        tokenValidator.validateTokenSignature(ticketToken)

        val ticketId = jwtTicketUtil.getTicketId(ticketToken)
        val ticket: Ticket = ticketService.getById(ticketId)
        val user: User = userService.getById(ticket.userId)

        validateBeforePaymentTicket(ticket.status)
        validateAlreadyEnterTicket(ticket.status)

        ticket.enter()

        return EnterUketEventResponse.of(ticket, user)
    }

    @Transactional(readOnly = true)
    fun searchLiveEnterUsers(uketEventId: Long, pageable: Pageable): Page<LiveEnterUserResponse> {
        val liveEnterUserDto: Page<LiveEnterUserDto> = ticketService.findLiveEnterTickets(uketEventId, pageable)
        return liveEnterUserDto.map {dto -> LiveEnterUserResponse.from(dto)}
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
