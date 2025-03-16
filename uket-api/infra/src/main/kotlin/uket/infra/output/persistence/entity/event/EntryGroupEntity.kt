package uket.infra.output.persistence.entity.event

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "entry_group")
@AttributeOverride(name = "id", column = Column(name = "entry_group_id"))
class EntryGroupEntity(
    uketEventRoundEntity: UketEventRoundEntity,
    name: String,
    entryStartTime: LocalDateTime,
    entryEndTime: LocalDateTime,
    reservationCount: Int,
    totalCount: Int
) : BaseTimeEntity() {

    @ManyToOne
    @JoinColumn(name = "uket_event_round_id", nullable = false)
    var uketEventRoundEntity: UketEventRoundEntity = uketEventRoundEntity
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
