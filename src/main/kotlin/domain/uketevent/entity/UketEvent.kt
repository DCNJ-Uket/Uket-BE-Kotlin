package uket.domain.uketevent.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.domain.BaseTimeEntity
import uket.uket.common.enums.EventType
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
