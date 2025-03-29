package uket.domain.reservation.repository

import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import uket.domain.reservation.entity.QTicket.ticket
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.uketevent.entity.QEntryGroup.entryGroup
import uket.domain.uketevent.entity.QUketEvent.uketEvent
import uket.domain.uketevent.entity.QUketEventRound.uketEventRound
import uket.domain.user.entity.QUser.user
import java.time.LocalDateTime

class TicketRepositoryCustomImpl(
    val queryFactory: JPAQueryFactory,
) : TicketRepositoryCustom {
    override fun findValidTicketsByUserId(userId: Long, statuses: List<TicketStatus>): List<Ticket> {
        val tickets = queryFactory
            .select(ticket)
            .from(ticket)
            .join(entryGroup)
            .on(entryGroup.id.eq(ticket.entryGroupId))
            .where(
                ticket.userId.eq(userId),
                ticket.status.notIn(statuses),
                entryGroup.uketEventRound.id.`in`(
                    JPAExpressions
                        .select(uketEventRound.id)
                        .from(uketEventRound)
                        .join(uketEventRound.uketEvent, uketEvent)
                        .where(uketEvent.displayEndDate.after(LocalDateTime.now())),
                ),
            ).fetch()
        return tickets
    }

    override fun findByDepositorName(depositorName: String, pageable: Pageable): Page<Ticket> {
        val count = queryFactory
            .select(ticket.count())
            .from(ticket)
            .join(user)
            .on(user.id.eq(ticket.userId))
            .where(user.depositorName.like("%$depositorName$%"))
            .fetchOne() ?: 0L

        val tickets = queryFactory
            .select(ticket)
            .from(ticket)
            .join(user)
            .on(user.id.eq(ticket.userId))
            .where(user.depositorName.like("%$depositorName$%"))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
        return PageImpl(tickets, pageable, count)
    }

    override fun findByPhoneNumberEndingWith(lastFourDigits: String, pageable: Pageable): Page<Ticket> {
        val count = queryFactory
            .select(ticket.count())
            .from(ticket)
            .join(user)
            .on(user.id.eq(ticket.userId))
            .where(user.phoneNumber.like("%$lastFourDigits"))
            .fetchOne() ?: 0L

        val tickets = queryFactory
            .select(ticket)
            .from(ticket)
            .join(user)
            .on(user.id.eq(ticket.userId))
            .where(user.phoneNumber.like("%$lastFourDigits"))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return PageImpl(tickets, pageable, count)
    }

    //    @Query("SELECT t FROM Ticket t WHERE t.show.startDate >= :showStart AND t.show.startDate < :showEnd")
    override fun findByUketEventRoundEventDateBetween(
        uketEventRoundStartDate: LocalDateTime,
        uketEventRoundEndDate: LocalDateTime,
        pageable: Pageable,
    ): Page<Ticket> {
        val count = queryFactory
            .select(ticket.count())
            .from(ticket)
            .join(entryGroup)
            .on(entryGroup.id.eq(ticket.entryGroupId))
            .where(
                entryGroup.id.`in`(
                    JPAExpressions
                        .select(entryGroup.id)
                        .from(entryGroup)
                        .join(entryGroup.uketEventRound, uketEventRound)
                        .where(uketEventRound.eventDate.between(uketEventRoundStartDate, uketEventRoundEndDate)),
                ),
            ).fetchOne() ?: 0L

        val tickets = queryFactory
            .select(ticket)
            .from(ticket)
            .join(entryGroup)
            .on(entryGroup.id.eq(ticket.entryGroupId))
            .where(
                entryGroup.id.`in`(
                    JPAExpressions
                        .select(entryGroup.id)
                        .from(entryGroup)
                        .join(entryGroup.uketEventRound, uketEventRound)
                        .where(uketEventRound.eventDate.between(uketEventRoundStartDate, uketEventRoundEndDate)),
                ),
            ).offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return PageImpl(tickets, pageable, count)
    }

    override fun findAllByUserIdAndStatusNotWithEntryGroup(userId: Long, status: TicketStatus): List<Ticket> {
        val tickets = queryFactory
            .select(ticket)
            .from(ticket)
            .join(entryGroup)
            .on(entryGroup.id.eq(ticket.id))
            .where(ticket.userId.eq(userId), ticket.status.eq(status))
            .fetch()

        return tickets
    }
}
