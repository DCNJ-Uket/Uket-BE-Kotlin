package uket.domain.eventregistration.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import uket.domain.BaseTimeEntity
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "event_round_registration")
class EventRoundRegistration(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_registration_id")
    private var eventRegistration: EventRegistration? = null,

    @Column(name = "event_round_date")
    val eventRoundDate: LocalDate,

    @Column(name = "event_start_time")
    val eventStartTime: LocalTime,
) : BaseTimeEntity()
