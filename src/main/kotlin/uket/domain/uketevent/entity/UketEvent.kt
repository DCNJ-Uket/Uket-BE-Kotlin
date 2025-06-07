package uket.domain.uketevent.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
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

    @Column(name = "total_ticket_count")
    val totalTicketCount: Int,

    @Column(name = "ticket_price")
    val ticketPrice: Int,

    @Column(name = "buy_ticket_limit")
    val buyTicketLimit: Int,

    @Embedded
    val details: EventDetails,

    @Column(name = "event_image_id")
    val eventImageId: String,

    @Column(name = "thumbnail_image_id")
    val thumbnailImageId: String,

    _banners: List<Banner>,
) : BaseTimeEntity() {
    fun addEventRound(uketEventRound: UketEventRound) {
        firstRoundDateTime = minOf(firstRoundDateTime, uketEventRound.eventRoundDateTime)
        lastRoundDateTime = maxOf(lastRoundDateTime, uketEventRound.eventRoundDateTime)
    }

    @Column(name = "first_round_datetime")
    var firstRoundDateTime: LocalDateTime = LocalDateTime.MAX

    @Column(name = "last_round_datetime")
    var lastRoundDateTime: LocalDateTime = LocalDateTime.MIN

    @OneToMany(
        mappedBy = "uketEvent",
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        cascade = [CascadeType.ALL],
    )
    var banners: List<Banner> = _banners.map {
        Banner(
            id = it.id,
            uketEvent = this,
            imageId = it.imageId,
            link = it.link
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
        @Column(name = "contact_link")
        val link: String?,
    ) {
        enum class ContactType {
            INSTAGRAM,
            KAKAO,
        }
    }
}
