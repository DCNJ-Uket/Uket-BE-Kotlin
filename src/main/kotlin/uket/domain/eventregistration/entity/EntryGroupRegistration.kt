package uket.domain.eventregistration.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import uket.domain.BaseTimeEntity
import java.time.LocalTime

@Entity
@Table(name = "entry_group_registration")
class EntryGroupRegistration(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val ticketCount: Int,
    val entryStartTime: LocalTime,
    val entryEndTime: LocalTime,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_registration_id")
    private var eventRegistration: EventRegistration? = null,
) : BaseTimeEntity()
