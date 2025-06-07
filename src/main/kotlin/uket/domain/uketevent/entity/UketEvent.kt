package uket.domain.uketevent.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.common.enums.EventContactType
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
    val ticketPrice: Long,

    @Column(name = "buy_ticket_limit")
    val buyTicketLimit: Int,

    @Embedded
    val details: EventDetails,

    @Column(name = "event_image_id")
    val eventImageId: String,

    @Column(name = "thumbnail_image_id")
    val thumbnailImageId: String,

    @Column(name = "first_round_datetime")
    val firstRoundDateTime: LocalDateTime,

    @Column(name = "last_round_datetime")
    val lastRoundDateTime: LocalDateTime,
) : BaseTimeEntity() {
    @Column(name = "is_visible")
    var isVisible: Boolean = false
        protected set

    @Column(name = "event_open_date_time")
    var eventOpenDateTime: LocalDateTime? = null
        protected set

    @Column(name = "event_finish_date_time")
    var eventFinishDateTime: LocalDateTime? = null
        protected set

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
        val type: EventContactType,
        @Column(name = "contact_content")
        val content: String,
        @Column(name = "contact_link")
        val link: String?,
    )

    fun open(now: LocalDateTime = LocalDateTime.now()) {
        this.isVisible = true
        this.eventOpenDateTime = now
    }

    fun finish(now: LocalDateTime = LocalDateTime.now()) {
        this.isVisible = false
        this.eventFinishDateTime = now
    }
}
