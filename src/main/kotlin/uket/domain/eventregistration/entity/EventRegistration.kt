package uket.domain.eventregistration.entity

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
import org.hibernate.annotations.BatchSize
import uket.common.LoggerDelegate
import uket.common.enums.BankCode
import uket.common.enums.EventContactType
import uket.common.enums.EventType
import uket.domain.BaseTimeEntity
import uket.domain.uketevent.entity.Banner
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "event_registration")
class EventRegistration(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "organization_id")
    val organizationId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: EventRegistrationStatus = EventRegistrationStatus.검수_진행,

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    val eventType: EventType,

    @Column(name = "event_name")
    val eventName: String,

    @Column(name = "location")
    val location: String,

    @Column(name = "event_start_date")
    val eventStartDate: LocalDate,

    @Column(name = "ticketing_end_date")
    val eventEndDate: LocalDate,

    @Column(name = "ticketing_start_date_time")
    val ticketingStartDateTime: LocalDateTime,

    @Column(name = "ticketing_end_date_time")
    val ticketingEndDateTime: LocalDateTime,

    @Column(name = "total_ticket_count")
    val totalTicketCount: Int,

    @Column(name = "ticket_buy_limit")
    val ticketBuyLimit: Int,

    @Embedded
    val details: EventDetails,

    @Embedded
    val paymentInfo: PaymentInfo,

    @Column(name = "event_image_id")
    val uketEventImageId: String,

    @Column(name = "thumbnail_image_id")
    val thumbnailImageId: String,

    _banners: List<BannerRegistration>,
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

    @Column(name = "uket_event_id")
    var uketEventId: Long? = null
        protected set

    @OneToMany(
        mappedBy = "eventRegistration",
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        cascade = [CascadeType.ALL],
    )
    @BatchSize(size = 10)
    val banners: List<BannerRegistration> = _banners.map {
        BannerRegistration(
            id = it.id,
            eventRegistration = this,
            imageId = it.imageId,
            link = it.link,
        )
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
    ) {
        fun toUketEvent(): UketEvent.EventDetails {
            return UketEvent.EventDetails(
                information = information,
                caution = caution,
                contact = contact.toUketEvent()
            )
        }
    }

    @Embeddable
    data class PaymentInfo(
        @Column(name = "ticket_price")
        val ticketPrice: Long,
        @Column(name = "bank_code")
        @Enumerated(EnumType.STRING)
        val bankCode: BankCode,
        @Column(name = "account_number")
        val accountNumber: String,
        @Column(name = "depositor_name")
        val depositorName: String,
        @Column(name = "deposit_url")
        val depositUrl: String,
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
    ) {
        fun toUketEvent(): UketEvent.EventContact {
            return UketEvent.EventContact(
                type = type,
                content = content,
                link = link
            )
        }
    }

    fun toUketEvent(): UketEvent {
        return UketEvent(
            organizationId = organizationId,
            eventName = eventName,
            eventType = eventType,
            location = location,
            totalTicketCount = totalTicketCount,
            ticketPrice = paymentInfo.ticketPrice,
            details = details.toUketEvent(),
            eventImageId = uketEventImageId,
            thumbnailImageId = thumbnailImageId,
            firstRoundDateTime = eventRound.minOf { LocalDateTime.of(it.eventRoundDate, it.eventStartTime) },
            lastRoundDateTime = eventRound.maxOf { LocalDateTime.of(it.eventRoundDate, it.eventStartTime) },
        )
    }

    fun toEventRounds(): List<UketEventRound> {
        return eventRound.map {
            UketEventRound(
                uketEventId = uketEventId!!,
                eventRoundDateTime = LocalDateTime.of(it.eventRoundDate, it.eventStartTime),
                ticketingStartDateTime = ticketingStartDateTime,
                ticketingEndDateTime = ticketingEndDateTime
            )
        }
    }

    fun toEntryGroups(uketEventRound: UketEventRound): List<EntryGroup> {
        return entryGroup.map {
            EntryGroup(
                uketEventId = uketEventId!!,
                uketEventRoundId = uketEventRound.id,
                entryStartDateTime = LocalDateTime.of(uketEventRound.eventRoundDateTime.toLocalDate(), it.entryStartTime),
                totalTicketCount = it.ticketCount
            )
        }
    }

    fun toBanners(): List<Banner> {
        return banners.map {
            Banner(
                uketEventId = uketEventId!!,
                imageId = it.imageId,
                link = it.link
            )
        }
    }

    fun updateStatus(registrationStatus: EventRegistrationStatus) {
        this.status = registrationStatus
    }

    fun updateId(registrationId: Long) {
        this.id = registrationId
    }

    fun settingEvent(uketEventId: Long) {
        this.uketEventId = uketEventId
    }

    fun clearUketEvent() {
        this.uketEventId = null
    }

    companion object {
        private val log by LoggerDelegate()
    }
}
