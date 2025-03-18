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
    id: Long,
    organizationId: Long,
    name: String,
    eventType: EventType,
    location: String,
    eventImagePath: String?,
    displayEndDate: LocalDateTime,
    ticketPrice: Int,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "uket_event_id")
    var id: Long = id

    @Column(nullable = false)
    var organizationId: Long = organizationId
        protected set

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var eventType: EventType = eventType
        protected set

    @Column(nullable = false)
    var location: String = location
        protected set

    var eventImagePath: String? = eventImagePath
        protected set

    @Column(nullable = false)
    var displayEndDate: LocalDateTime = displayEndDate
        protected set

    @Column(nullable = false)
    var ticketPrice: Int = ticketPrice
        protected set
}
