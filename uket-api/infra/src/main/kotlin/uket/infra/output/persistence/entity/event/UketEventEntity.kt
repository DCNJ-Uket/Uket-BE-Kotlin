package uket.infra.output.persistence.entity.event

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "uket_event_id"))
class UketEventEntity(
    organizationId: Long,
    name: String,
    eventType: EventType?,
    location: String,
    eventImagePath: String,
    displayEndDate: LocalDateTime,
    ticketPrice: Int
) : BaseTimeEntity() {
    var organizationId: Long = organizationId
        protected set
    var name: String = name
        protected set
    var eventType: EventType? = eventType
        protected set
    var location: String = location
        protected set
    var eventImagePath: String = eventImagePath
        protected set
    var displayEndDate: LocalDateTime = displayEndDate
        protected set
    var ticketPrice: Int = ticketPrice
        protected set
}
