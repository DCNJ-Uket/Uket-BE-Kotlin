package uket.infra.output.persistence.entity.event

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class UketEventEntity(
    var organizationId: Long = 0L,
    var name: String = "",
    var eventType: EventType? = null,
    var location: String = "",
    var eventImagePath: String = "",
    var displayEndDate: LocalDateTime = LocalDateTime.now(),
    var ticketPrice: Int = 0,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @Id
    @GeneratedValue
    @Column(name = "uket_event_id")
    private var id: Long = 0L

}
