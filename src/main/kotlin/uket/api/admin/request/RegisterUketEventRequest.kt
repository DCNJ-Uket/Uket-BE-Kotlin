package uket.api.admin.request

import uket.common.enums.EventType
import uket.domain.eventregistration.EventData
import uket.domain.eventregistration.entity.BannerRegistration
import uket.domain.eventregistration.entity.EntryGroupRegistration
import uket.domain.eventregistration.entity.EventRegistration
import uket.domain.eventregistration.entity.EventRegistrationStatus
import uket.domain.eventregistration.entity.EventRoundRegistration

data class RegisterUketEventRequest(
    val festivalData: EventData? = null,
    val performanceData: EventData? = null,
) {
    fun validateByEventType(eventType: EventType) {
        when (eventType) {
            EventType.FESTIVAL -> require(festivalData != null) {
                "[RegisterUketEventRequest] festivalData 가 null일 수 없습니다. | $eventType"
            }

            EventType.PERFORMANCE -> require(performanceData != null) {
                "[RegisterUketEventRequest] performanceData 가 null일 수 없습니다. | $eventType"
            }
        }
    }

    fun toEntity(originalEventRegistration: EventRegistration, eventType: EventType): EventRegistration {
        val eventData = when (eventType) {
            EventType.FESTIVAL -> this.festivalData!!
            EventType.PERFORMANCE -> this.performanceData!!
        }

        return with(eventData) {
            EventRegistration(
                eventType = eventType,
                eventName = eventName,
                organizationId = originalEventRegistration.organizationId,
                location = location,
                status = originalEventRegistration.status,
                ticketingStartDateTime = ticketingStartDateTime,
                ticketingEndDateTime = ticketingEndDateTime,
                eventStartDate = eventRound.minOf { it.date },
                eventEndDate = eventRound.maxOf { it.date },
                totalTicketCount = totalTicketCount,
                details = EventRegistration.EventDetails(
                    information = details.information,
                    caution = details.caution,
                    contact = EventRegistration.EventContact(
                        type = contact.type,
                        content = contact.content,
                        link = contact.link
                    ),
                ),
                uketEventImageId = uketEventImageId,
                thumbnailImageId = thumbnailImageId,
                _banners = banners.map {
                    BannerRegistration(
                        imageId = it.imageId, link = it.link
                    )
                },
                _eventRound = eventRound.map {
                    EventRoundRegistration(
                        eventRoundDate = it.date,
                        eventStartTime = it.startTime,
                    )
                },
                _entryGroup = entryGroup.map {
                    EntryGroupRegistration(
                        entryStartTime = it.entryStartTime,
                        ticketCount = it.ticketCount,
                    )
                },
                paymentInfo = EventRegistration.PaymentInfo(
                    ticketPrice = paymentInfo.ticketPrice,
                    bankCode = paymentInfo.bankCode,
                    accountNumber = paymentInfo.accountNumber,
                    depositorName = paymentInfo.depositorName,
                    depositUrl = paymentInfo.depositUrl
                ),
                buyTicketLimit = buyTicketLimit
            ).apply {
                val originalEventID = originalEventRegistration.uketEventId

                if(originalEventID != null) {
                    this.settingEvent(originalEventID)
                }
            }
        }
    }

    fun toEntity(organizationId: Long, status: EventRegistrationStatus, eventType: EventType): EventRegistration {
        val eventData = when (eventType) {
            EventType.FESTIVAL -> this.festivalData!!
            EventType.PERFORMANCE -> this.performanceData!!
        }

        return with(eventData) {
            EventRegistration(
                eventType = eventType,
                eventName = eventName,
                organizationId = organizationId,
                location = location,
                status = status,
                ticketingStartDateTime = ticketingStartDateTime,
                ticketingEndDateTime = ticketingEndDateTime,
                eventStartDate = eventRound.minOf { it.date },
                eventEndDate = eventRound.maxOf { it.date },
                totalTicketCount = totalTicketCount,
                details = EventRegistration.EventDetails(
                    information = details.information,
                    caution = details.caution,
                    contact = EventRegistration.EventContact(
                        type = contact.type,
                        content = contact.content,
                        link = contact.link
                    ),
                ),
                uketEventImageId = uketEventImageId,
                thumbnailImageId = thumbnailImageId,
                _banners = banners.map {
                    BannerRegistration(
                        imageId = it.imageId, link = it.link
                    )
                },
                _eventRound = eventRound.map {
                    EventRoundRegistration(
                        eventRoundDate = it.date,
                        eventStartTime = it.startTime,
                    )
                },
                _entryGroup = entryGroup.map {
                    EntryGroupRegistration(
                        entryStartTime = it.entryStartTime,
                        ticketCount = it.ticketCount,
                    )
                },
                paymentInfo = EventRegistration.PaymentInfo(
                    ticketPrice = paymentInfo.ticketPrice,
                    bankCode = paymentInfo.bankCode,
                    accountNumber = paymentInfo.accountNumber,
                    depositorName = paymentInfo.depositorName,
                    depositUrl = paymentInfo.depositUrl
                ),
                buyTicketLimit = buyTicketLimit
            )
        }
    }
}
