package uket.uket.facade

import org.springframework.stereotype.Service
import uket.common.ErrorLevel
import uket.common.PublicException
import uket.domain.reservation.dto.CreateTicketCommand
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.user.service.UserService
import uket.modules.redis.aop.DistributedLock
import java.time.LocalDateTime

@Service
class TicketingFacade(
    private val userService: UserService,
    private val ticketService: TicketService,
    private val entryGroupService: EntryGroupService,
) {
    private final val MAX_TICKETING_COUNT = 4

    @DistributedLock(key = "#entryGroupId")
    fun ticketing(userId: Long, entryGroupId: Long, ticketCount: Int, friend: String): Pair<List<Ticket>, UketEvent> {
        val now = LocalDateTime.now()

        val entryGroup = entryGroupService.getByIdWithUketEventRoundAndUketEvent(entryGroupId)
        val event = entryGroup.uketEventRound.uketEvent!!

        // validation
        userService.getById(userId)
        validateTicketingDateTime(event, entryGroup, now)
        validateTicketingCount(userId, entryGroup.uketEventRound.id, ticketCount)

        entryGroupService.increaseReservedCount(entryGroupId, ticketCount)
        val tickets = ticketService.publishTickets(CreateTicketCommand(userId, entryGroupId, TicketStatus.BEFORE_PAYMENT, friend), ticketCount)

        return Pair(tickets, event)
    }

    private fun validateTicketingCount(userId: Long, eventRoundId: Long, ticketCount: Int) {
        val tickets = ticketService.findAllActiveByUserAndEventRound(userId, eventRoundId)
        if (tickets.size + ticketCount > MAX_TICKETING_COUNT) {
            throw PublicException(
                publicMessage = "최대 예매 가능한 티켓 개수를 초과했습니다.",
                systemMessage = "[TicketingFacade] 최대 예매 가능 개수(${MAX_TICKETING_COUNT}) 초과 예매 시도",
                title = "최대 예매 가능 개수(${MAX_TICKETING_COUNT}) 초과 예매 시도",
                errorLevel = ErrorLevel.ERROR
            )
        }
    }

    private fun validateTicketingDateTime(
        event: UketEvent,
        entryGroup: EntryGroup,
        now: LocalDateTime,
    ) {
        event.validateNowTicketing(now)
        if (entryGroup.entryStartDateTime.isBefore(now)) {
            throw PublicException(
                publicMessage = "예매가 불가능한 시간대입니다.",
                systemMessage = "[TicketingFacade] 예매가 불가능한 entryGroup(id=${entryGroup.id})에 대한 예매 시도",
                title = "예매 불가능한 entryGruop에 대한 예매 시도",
                errorLevel = ErrorLevel.ERROR
            )
        }
    }
}
