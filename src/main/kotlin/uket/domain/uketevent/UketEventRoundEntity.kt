package uket.uket.domain.uketevent

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "uket_event_round")
@AttributeOverride(name = "id", column = Column(name = "uket_event_round_entity"))
class UketEventRoundEntity(
    uketEventEntity: UketEventEntity,
    name: String,
    eventDate: LocalDateTime,
    ticketingDateTime: LocalDateTime
) : BaseTimeEntity() {

    @ManyToOne
    @JoinColumn(name = "uket_event_id", nullable = false)
    var uketEntity: UketEventEntity = uketEventEntity
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
