package uket.api.admin.response

import uket.common.TimeUtils.toKr
import uket.common.enums.EventType
import uket.common.toResponseFormat
import uket.domain.admin.entity.Organization
import uket.domain.eventregistration.entity.EventRegistration
import java.time.LocalDate
import java.time.ZonedDateTime

data class UketEventRegistrationSummaryResponse(
    val organizationId: Long,
    val organizationName: String,
    val uketEventRegistrationId: Long,
    val eventName: String,
    val eventType: EventType,
    val eventStartDate: LocalDate,
    val eventEndDate: LocalDate,
    val ticketingStartDateTime: ZonedDateTime,
    val registrationStatus: String,
    val isModifiable: Boolean,
    val buyTicketLimit: Int,
) {
    companion object {
        fun of(eventRegistration: EventRegistration, organization: Organization): UketEventRegistrationSummaryResponse =
            UketEventRegistrationSummaryResponse(
                organizationId = eventRegistration.organizationId,
                organizationName = organization.name,
                uketEventRegistrationId = eventRegistration.id,
                eventName = eventRegistration.eventName,
                eventType = eventRegistration.eventType,
                eventStartDate = eventRegistration.eventStartDate,
                eventEndDate = eventRegistration.eventEndDate,
                ticketingStartDateTime = eventRegistration.ticketingStartDateTime.toKr(),
                registrationStatus = eventRegistration.status.toResponseFormat(),
                isModifiable = eventRegistration.status.isModifiable,
                buyTicketLimit = eventRegistration.buyTicketLimit
            )
    }
}
