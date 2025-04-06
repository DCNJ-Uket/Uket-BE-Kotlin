package uket.api

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uket.api.request.RegisterUketEventRequest
import uket.api.response.RegisterUketEventResponse
import uket.common.enums.EventType
import uket.domain.admin.service.OrganizationService
import uket.uket.domain.eventregistration.service.EventRegistrationService

@RestController
class EventRegistrationController(
    private val organizationService: OrganizationService,
    private val eventRegistrationService: EventRegistrationService,
) {
    @PostMapping("/admin/organizations/{organizationId}/uket-event-registrations/event-type/{eventType}")
    fun registerUketEvent(
        @RequestParam("organizationId") organizationId: Long,
        @PathVariable("eventType") eventType: EventType,
        @RequestBody request: RegisterUketEventRequest,
    ): RegisterUketEventResponse {
        request.validateByEventType(eventType)

        val organization = organizationService.findById(organizationId)

        val eventRegistration = eventRegistrationService.registerEvent(
            request.toEntity(organization.id, eventType),
        )
        return RegisterUketEventResponse(
            uketEventRegistrationId = eventRegistration.id,
            eventType = eventType,
        )
    }
}
