package uket.infra.output.persistence.entity.event

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class UketEventRoundEntity(
    var uketEventId: Long = 0L,
    var name: String = "",
    var eventDate: LocalDateTime = LocalDateTime.now(),
    var ticketingDateTime: LocalDateTime = LocalDateTime.now(),
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @Id
    @GeneratedValue
    @Column(name = "uket_event_round_entity")
    private var id: Long = 0L

}
