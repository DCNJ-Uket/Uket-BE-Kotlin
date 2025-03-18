package uket.uket.domain.uketevent

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "entry_group")
@AttributeOverride(name = "id", column = Column(name = "entry_group_id"))
class EntryGroup(
    uketEventRound: UketEventRound,
    name: String,
    entryStartTime: LocalDateTime,
    entryEndTime: LocalDateTime,
    reservationCount: Int,
    totalCount: Int
) : BaseTimeEntity() {

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
