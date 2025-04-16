package uket.api.admin

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uket.api.admin.request.RegisterUketEventRequest
import uket.api.admin.response.RegisterUketEventResponse
import uket.api.admin.response.UketRegistrationEventResponse
import uket.common.enums.EventType
import uket.domain.admin.service.OrganizationService
import uket.domain.eventregistration.service.EventRegistrationService

@RestController
class EventRegistrationController(
    private val organizationService: OrganizationService,
    private val eventRegistrationService: EventRegistrationService,
) {
    @GetMapping("/admin/uket-event-registrations")
    fun getUketEventRegistrations(): List<UketRegistrationEventResponse> {
        return listOf()
    }

    @GetMapping("/admin/uket-event-registrations/{uketEventRegistrationId}")
    fun getUketEventRegistration(
        @PathVariable("uketEventRegistrationId") uketEventRegistrationId: Long,
    ): UketRegistrationEventResponse {
        val eventRegistration = eventRegistrationService.getByIdWithEventRoundAndEntryGroup(uketEventRegistrationId)
        return UketRegistrationEventResponse.from(eventRegistration)
    }

    @PostMapping("/admin/uket-event-registrations/organizations/{organizationId}/event-type/{eventType}")
    fun registerUketEvent(
        @RequestParam("organizationId") organizationId: Long,
        @PathVariable("eventType") eventType: EventType,
        @RequestBody request: RegisterUketEventRequest,
    ): RegisterUketEventResponse {
        request.validateByEventType(eventType)

        val organization = organizationService.getById(organizationId)

        val eventRegistration = eventRegistrationService.registerEvent(
            request.toEntity(organization.id, eventType),
        )
        return RegisterUketEventResponse(
            uketEventRegistrationId = eventRegistration.id,
            eventType = eventType,
        )
    }
}
