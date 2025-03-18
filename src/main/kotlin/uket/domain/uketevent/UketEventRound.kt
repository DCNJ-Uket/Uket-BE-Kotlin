package uket.uket.domain.uketevent

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "uket_event_round")
class UketEventRound(
    id: Long,
    uketEvent: UketEvent,
    name: String,
    eventDate: LocalDateTime,
    ticketingDateTime: LocalDateTime
) : BaseTimeEntity() {

    @Id @GeneratedValue
    @Column(name = "uket_event_round_id")
    var id: Long = id

    @ManyToOne
    @JoinColumn(name = "uket_event_id", nullable = false)
    var uketEvent: UketEvent = uketEvent
        protected set

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var eventDate: LocalDateTime = eventDate
        protected set

    @Column(nullable = false)
    var ticketingDateTime: LocalDateTime = ticketingDateTime
        protected set
}
