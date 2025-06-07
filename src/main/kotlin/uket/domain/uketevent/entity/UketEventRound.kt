package uket.domain.uketevent.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "uket_event_round")
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
    fun isNowTicketing(at: LocalDateTime): Boolean = !(at.isBefore(this.ticketingStartDateTime) || at.isAfter(this.ticketingEndDateTime))

    fun isTicketingEnd(at: LocalDateTime): Boolean = at.isAfter(ticketingEndDateTime)
}
