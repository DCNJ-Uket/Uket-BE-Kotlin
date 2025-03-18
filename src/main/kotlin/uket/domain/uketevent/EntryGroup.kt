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
    id: Long,
    uketEventRound: UketEventRound,
    name: String,
    entryStartTime: LocalDateTime,
    entryEndTime: LocalDateTime,
    reservationCount: Int,
    totalCount: Int,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "entry_group_id")
    var id: Long = id

    @ManyToOne
    @JoinColumn(name = "uket_event_round_id", nullable = false)
    var uketEventRound: UketEventRound = uketEventRound
        protected set

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var entryStartTime: LocalDateTime = entryStartTime
        protected set

    @Column(nullable = false)
    var entryEndTime: LocalDateTime = entryEndTime
        protected set

    @Column(nullable = false)
    var reservationCount: Int = reservationCount
        protected set

    @Column(nullable = false)
    var totalCount: Int = totalCount
        protected set
}
