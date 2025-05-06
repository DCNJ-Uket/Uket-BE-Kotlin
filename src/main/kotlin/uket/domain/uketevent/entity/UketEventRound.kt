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
import uket.common.PublicException
import uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "uket_event_round")
class UketEventRound(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uket_event_id")
    var uketEvent: UketEvent?,

    @Column(name = "event_round_datetime")
    val eventRoundDateTime: LocalDateTime,

    @Column(name = "ticketing_start_datetime")
    val ticketingStartDateTime: LocalDateTime,

    @Column(name = "ticketing_end_datetime")
    val ticketingEndDateTime: LocalDateTime,
) : BaseTimeEntity() {
    fun validateNowTicketing(now: LocalDateTime) {
        if (now.isBefore(this.ticketingStartDateTime) || now.isAfter(this.ticketingEndDateTime)) {
            throw PublicException(
                publicMessage = "예매가 불가능 한 회차입니다",
                systemMessage = "[UketEventRound] 티켓팅이 진행 중이지 않은 UketEventRound${this.id} validation",
                title = "예매 불가능 한 행사"
            )
        }
    }
}
