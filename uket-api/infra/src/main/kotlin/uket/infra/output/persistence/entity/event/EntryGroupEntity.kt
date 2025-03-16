package uket.infra.output.persistence.entity.event

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "entry_group_id"))
class EntryGroupEntity(
    uketEventRoundId: Long,
    name: String,
    entryStartTime: LocalDateTime,
    entryEndTime: LocalDateTime,
    reservationCount: Int,
    totalCount: Int
) : BaseTimeEntity() {
    var uketEventRoundId: Long = uketEventRoundId
        protected set
    var name: String = name
        protected set
    var entryStartTime: LocalDateTime = entryStartTime
        protected set
    var entryEndTime: LocalDateTime = entryEndTime
        protected set
    var reservationCount: Int = reservationCount
        protected set
    var totalCount: Int = totalCount
        protected set
}
