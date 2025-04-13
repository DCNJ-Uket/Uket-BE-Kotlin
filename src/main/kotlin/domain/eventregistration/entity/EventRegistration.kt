package uket.domain.eventregistration.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.BatchSize
import uket.common.LoggerDelegate
import uket.common.enums.EventType
import uket.domain.BaseTimeEntity
import uket.domain.eventregistration.converter.ListToStringConverter
import java.time.LocalDateTime

@Entity
@Table(name = "event_registration")
class EventRegistration(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "organization_id")
    val organizationId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    val status: EventRegistrationStatus = EventRegistrationStatus.검수진행,

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    val eventType: EventType,

    @Column(name = "event_name")
    val eventName: String,

    @Column(name = "location")
    val location: String,

    @Column(name = "ticketing_start_date_time")
    val ticketingStartDateTime: LocalDateTime,

    @Column(name = "ticketing_end_date_time")
    val ticketingEndDateTime: LocalDateTime,

    @Column(name = "total_ticket_count")
    val totalTicketCount: Int,

    @Embedded
    val details: EventDetails,

    @Column(name = "uket_event_image_id")
    val uketEventImageId: String,

    @Column(name = "thumbnail_image_id")
    val thumbnailImageId: String,

    @Convert(converter = ListToStringConverter::class)
    @Column(name = "banner_image_ids")
    val bannerImageIds: List<String>,

    _eventRound: List<EventRoundRegistration>,
    _entryGroup: List<EntryGroupRegistration>,
) : BaseTimeEntity() {
    init {
        check(ticketingStartDateTime < ticketingEndDateTime) {
            val message = "[EventRegistration] 티켓팅 시작시간은 종료시간보다 이전이어야 합니다. | ticketingStartDateTime: $ticketingStartDateTime, ticketingEndDateTime: $ticketingEndDateTime"
            log.warn(message)
            message
        }
    }

    @OneToMany(
        mappedBy = "eventRegistration",
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        cascade = [CascadeType.ALL],
    )
    @BatchSize(size = 10)
    val eventRound: List<EventRoundRegistration> = _eventRound.map {
        EventRoundRegistration(
            id = it.id,
            eventRegistration = this,
            eventRoundDate = it.eventRoundDate,
            eventStartTime = it.eventStartTime,
        )
    }

    @OneToMany(
        mappedBy = "eventRegistration",
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        cascade = [CascadeType.ALL],
    )
    @BatchSize(size = 10)
    val entryGroup: List<EntryGroupRegistration> = _entryGroup.map {
        EntryGroupRegistration(
            id = it.id,
            eventRegistration = this,
            entryStartTime = it.entryStartTime,
            entryEndTime = it.entryEndTime,
            ticketCount = it.ticketCount,
        )
    }

    @Embeddable
    data class EventDetails(
        @Column(name = "information")
        val information: String,
        @Column(name = "caution")
        val caution: String,
        @Embedded
        val contact: EventContact,
    )

    @Embeddable
    data class EventContact(
        @Column(name = "contact_type")
        @Enumerated(EnumType.STRING)
        val type: ContactType,
        @Column(name = "contact_content")
        val content: String,
    ) {
        enum class ContactType {
            INSTAGRAM,
            KAKAO,
        }
    }

    companion object {
        private val log by uket.common.LoggerDelegate()
    }
}
