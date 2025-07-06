package uket.domain.uketevent.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import uket.common.ErrorLevel
import uket.common.PublicException
import uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(
    name = "entry_group",
    indexes = [
        Index(name = "index_entry_group_01", columnList = "uketEventId"),
        Index(name = "index_entry_group_02", columnList = "uketEventRoundId"),
    ]
)
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
    fun increaseReservedCount(count: Int) {
        if (this.ticketCount + count > this.totalTicketCount) {
            throw PublicException(
                publicMessage = "해당 입장 그룹의 예매 가능 인원이 부족합니다.",
                systemMessage = "예매 가능 인원이 부족한 입장 그룹에 대한 예매 시도 | entryGroup=${this.id}",
                title = "예매 가능 인원이 부족한 입장 그룹에 대한 예매 시도",
                errorLevel = ErrorLevel.WARN
            )
        }

        this.ticketCount += count
    }

    fun decreaseReservedCount(): Boolean {
        if (this.ticketCount < 1) {
            return false
        }

        this.ticketCount -= 1
        return true
    }
}
