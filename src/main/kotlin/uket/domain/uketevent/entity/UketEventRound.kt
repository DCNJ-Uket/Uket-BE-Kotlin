package uket.uket.domain.uketevent.entity

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "uket_event_round")
class UketEventRound(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "uket_event_id", nullable = false)
    val uketEvent: UketEvent,
    val name: String,
    val eventDate: LocalDateTime,
    val ticketingDateTime: LocalDateTime,
) : BaseTimeEntity()
