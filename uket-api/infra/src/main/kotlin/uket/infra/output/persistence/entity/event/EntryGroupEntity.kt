package uket.infra.output.persistence.entity.event

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "entry_group_id"))
class EntryGroupEntity(
    var uketEventRoundId: Long = 0L,
    var name: String = "",
    var entryStartTime: LocalDateTime = LocalDateTime.now(),
    var entryEndTime: LocalDateTime = LocalDateTime.now(),
    var reservationCount: Int = 0,
    var totalCount: Int = 0
) : BaseTimeEntity() {
}
