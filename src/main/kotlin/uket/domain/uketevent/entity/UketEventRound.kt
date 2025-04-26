package uket.domain.uketevent.entity

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
import java.time.LocalDateTime

@Entity
@Table(name = "uket_event_round")
class UketEventRound(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uket_event_id", nullable = false)
    var uketEvent: UketEvent?,

    @Column(name = "event_round_datetime")
    val eventRoundDateTime: LocalDateTime,
) : BaseTimeEntity()
