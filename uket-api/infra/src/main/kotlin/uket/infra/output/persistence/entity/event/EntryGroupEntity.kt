package uket.infra.output.persistence.entity.event

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class EntryGroupEntity(
    var uketEventRoundId: Long = 0L,
    var name: String = "",
    var entryStartTime: LocalDateTime = LocalDateTime.now(),
    var entryEndTime: LocalDateTime = LocalDateTime.now(),
    var reservationCount: Int = 0,
    var totalCount: Int = 0,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @Id
    @GeneratedValue
    @Column(name = "entry_group_id")
    private var id: Long = 0L

}
