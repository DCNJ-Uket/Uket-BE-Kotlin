package uket.uket.domain.uketeventregistration.entity

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
import uket.domain.BaseTimeEntity
import uket.domain.eventregistration.entity.EventRoundRegistration
import uket.uket.common.enums.EventType
import uket.uket.domain.eventregistration.converter.ListToStringConverter
import uket.uket.domain.eventregistration.entity.EventRegistrationStatus
import java.time.LocalDateTime

@Entity
@Table(name = "event_registration")
class EventRegistration(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    val status: EventRegistrationStatus,
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
    @OneToMany(
        mappedBy = "eventRegistration",
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        cascade = [CascadeType.ALL],
    )
    val eventRound: List<EventRoundRegistration>,
    @OneToMany(
        mappedBy = "eventRegistration",
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        cascade = [CascadeType.ALL],
    )
    val entryGroup: List<EntryGroupRegistration>,
    @Embedded
    val details: EventDetails,
    @Embedded
    val contact: EventContact,
    @Column(name = "uket_event_image_id")
    val uketEventImageId: String,
    @Column(name = "thumbnail_image_id")
    val thumbnailImageId: String,
    @Convert(converter = ListToStringConverter::class)
    @Column(name = "banner_image_ids")
    val bannerImageIds: List<String>,
) : BaseTimeEntity() {
    @Embeddable
    data class EventDetails(
        val information: String,
        val caution: String,
        val contact: EventContact,
    )

    @Embeddable
    data class EventContact(
        val type: ContactType,
        val content: String,
    ) {
        enum class ContactType {
            INSTAGRAM,
            KAKAO,
        }
    }
}
