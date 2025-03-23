package uket.uket.domain.uketevent.entity

import jakarta.persistence.*
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
    var reservationCount: Int,
    val totalCount: Int,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "entry_group_id")
    val id: Long = _id

    @ManyToOne
    @JoinColumn(name = "uket_event_round_id", nullable = false)
    val uketEventRound: UketEventRound = _uketEventRound

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
