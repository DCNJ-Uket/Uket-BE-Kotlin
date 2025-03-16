package uket.infra.output.persistence.entity.event

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
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
