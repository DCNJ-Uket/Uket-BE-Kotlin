package uket.domain.uketevent.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(
    name = "uket_event_round",
    indexes = [Index(name = "index_uket_event_round_01", columnList = "uketEventId")]
)
class UketEventRound(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "uket_event_id")
    var uketEventId: Long,

    @Column(name = "event_round_datetime")
    val eventRoundDateTime: LocalDateTime,

    @Column(name = "ticketing_start_datetime")
    val ticketingStartDateTime: LocalDateTime,

    @Column(name = "ticketing_end_datetime")
    val ticketingEndDateTime: LocalDateTime,
) : BaseTimeEntity() {
    // TODO 7/17 공연에 대해서는 DB에 직접 주입하는 값
    @Column(name = "ticket_cancel_end_date_time")
    val ticketCancelEndDateTime: LocalDateTime? = null

    fun isCancelable(at: LocalDateTime): Boolean = at <= ticketCancelEndDateTime

    fun isNowTicketing(at: LocalDateTime): Boolean = !(at.isBefore(this.ticketingStartDateTime) || at.isAfter(this.ticketingEndDateTime))

    fun isTicketingEnd(at: LocalDateTime): Boolean = at.isAfter(ticketingEndDateTime)
}
