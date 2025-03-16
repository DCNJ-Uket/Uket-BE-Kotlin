package uket.infra.output.persistence.entity.event

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "uket_event_round_entity"))
class UketEventRoundEntity(
    var uketEventId: Long = 0L,
    var name: String = "",
    var eventDate: LocalDateTime = LocalDateTime.now(),
    var ticketingDateTime: LocalDateTime = LocalDateTime.now()
) : BaseTimeEntity() {
}
