package uket.domain.uketevent.entity

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
import uket.common.enums.EventType
import uket.domain.BaseTimeEntity
import uket.domain.uketevent.converter.ListToStringConverter
import java.time.LocalDateTime

@Entity
@Table(name = "uket_event")
class UketEvent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "organization_id")
    val organizationId: Long,

    @Column(name = "event_name")
    val eventName: String,

    @Enumerated(EnumType.STRING)
    val eventType: EventType,

    @Column(name = "location")
    val location: String,

    @Column(name = "ticketing_start_datetime")
    val ticketingStartDateTime: LocalDateTime,

    @Column(name = "ticketing_end_datetime")
    val ticketingEndDateTime: LocalDateTime,

    @Column(name = "ticket_price")
    val ticketPrice: Int,

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

    _uketEventRounds: List<UketEventRound>,
) : BaseTimeEntity() {
    @OneToMany(
        mappedBy = "uketEvent",
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        cascade = [CascadeType.ALL],
    )
    var uketEventRounds: List<UketEventRound> = _uketEventRounds.map {
        UketEventRound(
            id = it.id,
            uketEvent = this,
            eventRoundDateTime = it.eventRoundDateTime
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

    fun addUketEventRound(uketEventRound: UketEventRound) {
        this.uketEventRounds += uketEventRound
        uketEventRound.uketEvent = this
    }
}
