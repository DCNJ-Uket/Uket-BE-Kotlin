package uket.facade

import org.springframework.stereotype.Service
import uket.api.user.response.UserTicketResponse
import uket.domain.admin.entity.Organization
import uket.domain.admin.service.OrganizationService
import uket.domain.payment.entity.Payment
import uket.domain.payment.service.PaymentService
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.uketevent.service.UketEventRoundService
import uket.domain.uketevent.service.UketEventService
import uket.domain.user.service.UserService
import java.time.LocalDateTime

@Service
class FindUserTicketsFacade(
    private val ticketService: TicketService,
    private val entryGroupService: EntryGroupService,
    private val uketEventService: UketEventService,
    private val uketEventRoundService: UketEventRoundService,
    private val userService: UserService,
    private val organizationService: OrganizationService,
    private val paymentService: PaymentService,
) {
    fun findUserTickets(userId: Long, at: LocalDateTime): List<UserTicketResponse> {
        val tickets = ticketService.findAllByUserId(userId)
        val sortedTickets = tickets.sortedWith(ticketSortComparator())

        val user = userService.getById(userId)

        val entryGroupMap = getEntryGroupMap(sortedTickets)
        val eventRoundMap = getEventRoundMap(entryGroupMap)
        val eventMap = getEventMap(entryGroupMap)
        val organizationMap = getOrganizationMap(eventMap)
        val paymentMap = getPaymentMap(eventMap)

        val responses = sortedTickets.map { ticket ->
            val entryGroup = entryGroupMap[ticket.entryGroupId]!!
            val eventRound = eventRoundMap[entryGroup.uketEventRoundId]!!
            val event = eventMap[entryGroup.uketEventId]!!
            val org = organizationMap[event.organizationId]!!
            val payment = paymentMap[event.paymentId]!!

            UserTicketResponse.of(
                ticket = ticket,
                user = user,
                entryGroup = entryGroup,
                organization = org,
                event = event,
                isCancelable = eventRound.isCancelable(at),
                payment = payment
            )
        }

        return responses
    }

    private fun getOrganizationMap(eventMap: Map<Long, UketEvent>): Map<Long, Organization> {
        val organizationIds = eventMap.values.map { it.organizationId }.toSet()
        val organizationMap = organizationIds.map { organizationService.getById(it) }.associateBy { it.id }
        return organizationMap
    }

    private fun getEventMap(entryGroupMap: Map<Long, EntryGroup>): Map<Long, UketEvent> {
        val eventIds = entryGroupMap.values.map { it.uketEventId }.toSet()
        val eventMap = eventIds.map { uketEventService.getById(it) }.associateBy { it.id }
        return eventMap
    }

    private fun getEventRoundMap(entryGroupMap: Map<Long, EntryGroup>): Map<Long, UketEventRound> {
        val eventRoundIds = entryGroupMap.values.map { it.uketEventRoundId }.toSet()
        val eventRoundMap = eventRoundIds.map { uketEventRoundService.getById(it) }.associateBy { it.id }
        return eventRoundMap
    }

    private fun getEntryGroupMap(sortedTickets: List<Ticket>): Map<Long, EntryGroup> {
        val entryGroupIds = sortedTickets.map { it.entryGroupId }.toSet()
        val entryGroupMap = entryGroupIds.map { entryGroupService.getById(it) }.associateBy { it.id }
        return entryGroupMap
    }

    private fun getPaymentMap(eventMap: Map<Long, UketEvent>): Map<Long, Payment> {
        val paymentIds = eventMap.values.map { it.paymentId }.toSet()
        return paymentIds
            .map { paymentService.getByIdWithAccount(it) }
            .associateBy { it.id }
    }

    private fun ticketSortComparator() = compareBy<Ticket> {
        when (it.status) {
            TicketStatus.BEFORE_PAYMENT -> 1
            TicketStatus.BEFORE_ENTER -> 2
            TicketStatus.FINISH_ENTER -> 3
            TicketStatus.RESERVATION_CANCEL -> 4
            TicketStatus.EXPIRED -> 5
            TicketStatus.REFUND_REQUESTED -> 6
        }
    }
}
