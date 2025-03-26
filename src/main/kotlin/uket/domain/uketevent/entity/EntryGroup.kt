package uket.uket.domain.uketevent.entity

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "entry_group")
class EntryGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "uket_event_round_id", nullable = false)
    val uketEventRound: UketEventRound,
    val name: String,
    val entryStartTime: LocalDateTime,
    val entryEndTime: LocalDateTime,
    var reservationCount: Int,
    val totalCount: Int,
) : BaseTimeEntity() {
    fun increaseReservedCount(): Boolean {
        if (this.reservationCount + 1 > this.totalCount) {
            return false
        }

        this.reservationCount += 1
        return true
    }

    fun decreaseReservedCount(): Boolean {
        if (this.reservationCount < 1) {
            return false
        }

        this.reservationCount -= 1
        return true
    }
}
