package uket.infra.output.persistence.entity.event

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "uket_event_round_entity"))
class UketEventRoundEntity(
    uketEventId: Long,
    name: String,
    eventDate: LocalDateTime,
    ticketingDateTime: LocalDateTime
) : BaseTimeEntity() {
    var uketEventId: Long = uketEventId
        protected set
    var name: String = name
        protected set
    var eventDate: LocalDateTime = eventDate
        protected set
    var ticketingDateTime: LocalDateTime = ticketingDateTime
        protected set
}
