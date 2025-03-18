package uket.uket.domain.uketevent

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "uket_event_round")
class UketEventRound(
    _id: Long,
    _uketEvent: UketEvent,
    val name: String,
    val eventDate: LocalDateTime,
    val ticketingDateTime: LocalDateTime,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "uket_event_round_id")
    val id: Long = _id

    @ManyToOne
    @JoinColumn(name = "uket_event_id", nullable = false)
    val uketEvent: UketEvent = _uketEvent
}
