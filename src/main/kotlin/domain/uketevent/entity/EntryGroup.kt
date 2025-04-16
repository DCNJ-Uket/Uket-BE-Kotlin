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
@Table(name = "entry_group")
class EntryGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uket_event_round_id", nullable = false)
    val uketEventRound: UketEventRound,

    @Column(name = "entry_group_name")
    val entryGroupName: String,

    @Column(name = "entry_start_datetime")
    val entryStartDateTime: LocalDateTime,

    @Column(name = "entry_end_datetime")
    val entryEndDateTime: LocalDateTime,

    @Column(name = "ticket_count")
    var ticketCount: Int,
) : BaseTimeEntity() {
//    fun increaseReservedCount(): Boolean {
//        if (this.ticketCount + 1 > this.totalCount) {
//            return false
//        }
//
//        this.ticketCount += 1
//        return true
//    }

    fun decreaseReservedCount(): Boolean {
        if (this.ticketCount < 1) {
            return false
        }

        this.ticketCount -= 1
        return true
    }
}
