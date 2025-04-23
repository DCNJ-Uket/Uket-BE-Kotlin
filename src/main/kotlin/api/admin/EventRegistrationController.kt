package uket.api.admin

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uket.api.admin.request.RegisterUketEventRequest
import uket.api.admin.response.RegisterUketEventResponse
import uket.api.admin.response.UketEventRegistrationSummaryResponse
import uket.api.admin.response.UketRegistrationEventResponse
import uket.common.enums.EventType
import uket.common.response.CustomPageResponse
import uket.common.toEnum
import uket.domain.admin.service.OrganizationService
import uket.domain.eventregistration.service.EventRegistrationService

@RestController
class EventRegistrationController(
    private val organizationService: OrganizationService,
    private val eventRegistrationService: EventRegistrationService,
) {
    @GetMapping("/admin/uket-event-registrations")
    fun getUketEventRegistrations(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): CustomPageResponse<UketEventRegistrationSummaryResponse> {
        val pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"))

        val uketEventRegistrationSummaryResponse = eventRegistrationService.findAll(pageRequest).map {
            UketEventRegistrationSummaryResponse.from(it)
        }
        return CustomPageResponse(uketEventRegistrationSummaryResponse)
    }

    @GetMapping("/admin/uket-event-registrations/{uketEventRegistrationId}")
    fun getUketEventRegistration(
        @PathVariable("uketEventRegistrationId") uketEventRegistrationId: Long,
    ): UketRegistrationEventResponse {
        val eventRegistration = eventRegistrationService.getByIdWithEventRoundAndEntryGroup(uketEventRegistrationId)
        return UketRegistrationEventResponse.from(eventRegistration)
    }

    @PutMapping("/admin/uket-event-registrations/{uketEventRegistrationId}/status/{registrationStatus}")
    fun changeRegistrationStatus(
        @PathVariable("uketEventRegistrationId") uketEventRegistrationId: Long,
        @PathVariable("registrationStatus") registrationStatusString: String,
    ): ChangeEventRegistrationStatusResponse {
        // TODO(영준): Admin이 슈퍼 어드민인지 체크 필요

        val eventRegistration = eventRegistrationService.updateStatus(
            id = uketEventRegistrationId,
            registrationStatus = registrationStatusString.toEnum()
        )

        return ChangeEventRegistrationStatusResponse(
            uketEventRegistrationId = eventRegistration.id,
            currentStatus = eventRegistration.status
        )
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
