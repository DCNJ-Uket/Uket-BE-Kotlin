package uket.uket.domain.uketevent.entity

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity
import uket.uket.domain.uketevent.enums.EventType
import java.time.LocalDateTime

@Entity
@Table(name = "uket_event")
class UketEvent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val organizationId: Long,
    val name: String,
    @Enumerated(EnumType.STRING)
    val eventType: EventType,
    val location: String,
    val eventImagePath: String?,
    val displayEndDate: LocalDateTime,
    val ticketPrice: Int,
) : BaseTimeEntity()
