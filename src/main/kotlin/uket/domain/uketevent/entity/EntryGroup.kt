package uket.domain.uketevent.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "entry_group")
class EntryGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "uket_event_id")
    val uketEventId: Long,

    @Column(name = "uket_event_round_id")
    val uketEventRoundId: Long,

    @Column(name = "entry_start_datetime")
    val entryStartDateTime: LocalDateTime,

    @Column(name = "ticket_count")
    var ticketCount: Int = 0,

    @Column(name = "total_ticket_count")
    var totalTicketCount: Int,
) : BaseTimeEntity() {
    fun increaseReservedCount(): Boolean {
        if (this.ticketCount + 1 > this.totalTicketCount) {
            return false
        }

        this.ticketCount += 1
        return true
    }

    fun decreaseReservedCount(): Boolean {
        if (this.ticketCount < 1) {
            return false
        }

        this.ticketCount -= 1
        return true
    }
}
