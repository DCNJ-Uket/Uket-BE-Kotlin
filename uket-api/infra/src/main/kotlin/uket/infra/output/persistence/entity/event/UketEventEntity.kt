package uket.infra.output.persistence.entity.event

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
class UketEventEntity(
    var organizationId: Long = 0L,
    var name: String = "",
    var eventType: EventType? = null,
    var location: String = "",
    var eventImagePath: String = "",
    var displayEndDate: LocalDateTime = LocalDateTime.now(),
    var ticketPrice: Int = 0
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "uket_event_id")
    private var id: Long = 0L

}
