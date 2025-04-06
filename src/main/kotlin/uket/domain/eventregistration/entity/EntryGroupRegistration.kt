package uket.domain.eventregistration.entity

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
import uket.uket.common.LoggerDelegate
import java.time.LocalTime

@Entity
@Table(name = "entry_group_registration")
class EntryGroupRegistration(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "ticket_count")
    val ticketCount: Int,

    @Column(name = "entry_start_time")
    val entryStartTime: LocalTime,

    @Column(name = "entry_end_time")
    val entryEndTime: LocalTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_registration_id")
    private var eventRegistration: EventRegistration? = null,
) : BaseTimeEntity() {
    init {
        check(entryStartTime < entryEndTime) {
            val message = "[EventRegistration] 입장 시작시간은 종료시간보다 이전이어야 합니다. | entryStartTime: $entryStartTime, entryEndTime: $entryEndTime"
            log.warn(message)
            message
        }
    }

    companion object {
        private val log by LoggerDelegate()
    }
}
