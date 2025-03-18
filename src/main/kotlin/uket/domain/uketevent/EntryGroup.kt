package uket.uket.domain.uketevent

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "entry_group")
class EntryGroup(
    _id: Long,
    _uketEventRound: UketEventRound,
    val name: String,
    val entryStartTime: LocalDateTime,
    val entryEndTime: LocalDateTime,
    val reservationCount: Int,
    val totalCount: Int,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "entry_group_id")
    val id: Long = _id

    @ManyToOne
    @JoinColumn(name = "uket_event_round_id", nullable = false)
    val uketEventRound: UketEventRound = _uketEventRound
}
