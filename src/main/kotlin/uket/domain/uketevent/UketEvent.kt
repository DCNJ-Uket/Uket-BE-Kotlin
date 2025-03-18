package uket.uket.domain.uketevent

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "uket_event")
class UketEvent(
    _id: Long,
    val organizationId: Long,
    val name: String,
    val eventType: EventType,
    val location: String,
    val eventImagePath: String?,
    val displayEndDate: LocalDateTime,
    val ticketPrice: Int,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "uket_event_id")
    val id: Long = _id
}
