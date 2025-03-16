package uket.infra.output.persistence.entity.event

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "uket_event_id"))
class UketEventEntity(
    var organizationId: Long = 0L,
    var name: String = "",
    var eventType: EventType? = null,
    var location: String = "",
    var eventImagePath: String = "",
    var displayEndDate: LocalDateTime = LocalDateTime.now(),
    var ticketPrice: Int = 0
) : BaseTimeEntity() {
}
