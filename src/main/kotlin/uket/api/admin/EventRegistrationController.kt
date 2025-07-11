package uket.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uket.api.admin.request.RegisterUketEventRequest
import uket.api.admin.request.UploadUketEventImagesReqeust
import uket.api.admin.response.ChangeEventRegistrationStatusResponse
import uket.api.admin.response.EventImageUploadResponse
import uket.api.admin.response.RegisterUketEventResponse
import uket.api.admin.response.UketEventRegistrationSummaryResponse
import uket.api.admin.response.UketRegistrationEventResponse
import uket.auth.config.adminId.LoginAdminId
import uket.common.enums.EventType
import uket.common.response.CustomPageResponse
import uket.common.toEnum
import uket.domain.admin.service.AdminService
import uket.domain.admin.service.OrganizationService
import uket.domain.eventregistration.entity.EventRegistrationStatus
import uket.domain.eventregistration.service.EventRegistrationService
import uket.domain.eventregistration.service.EventRegistrationStatusStateResolver
import uket.facade.S3ImageFacade
import uket.facade.eventregistration.ModifyEventRegistrationFacade

@Tag(name = "어드민 행사 관련 API", description = "어드민 행사 관련 API 입니다.")
@RestController
class EventRegistrationController(
    private val organizationService: OrganizationService,
    private val eventRegistrationService: EventRegistrationService,
    private val s3ImageFacade: S3ImageFacade,
    private val adminService: AdminService,
    private val eventRegistrationStatusStateResolver: EventRegistrationStatusStateResolver,
    private val modifyEventRegistrationFacade: ModifyEventRegistrationFacade,
) {
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "어드민 행사 사진 업로드", description = "행사를 등록하기전 해당 행사의 사진들을 먼저 등록합니다.")
    @PostMapping("/admin/upload/images", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadUketEventImages(
        @ModelAttribute request: UploadUketEventImagesReqeust,
    ): ResponseEntity<EventImageUploadResponse> {
        val response: EventImageUploadResponse =
            s3ImageFacade.uploadUketEventImages(request.eventImage, request.thumbnailImage, request.bannerImages)
        return ResponseEntity.ok(response)
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "어드민 행사 등록", description = "사진 등록 후 반환받은 imageId를 활용해 행사 등록을 진행합니다.")
    @PostMapping("/admin/uket-event-registrations/organizations/{organizationId}/event-type/{eventType}")
    fun registerUketEvent(
        @PathVariable("organizationId") organizationId: Long,
        @PathVariable("eventType") eventType: EventType,
        @RequestBody request: RegisterUketEventRequest,
    ): RegisterUketEventResponse {
        request.validateByEventType(eventType)

        val organization = organizationService.getById(organizationId)

        val eventRegistration = eventRegistrationService.registerEvent(
            request.toEntity(organization.id, EventRegistrationStatus.검수_진행, eventType),
        )
        return RegisterUketEventResponse(
            uketEventRegistrationId = eventRegistration.id,
            eventType = eventType,
        )
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "어드민 행사 수정 등록", description = "사진 등록 후 반환받은 imageId를 활용해 행사 수정 등록을 진행합니다.")
    @PutMapping("/admin/uket-event-registrations/{uketEventRegistrationId}/event-type/{eventType}")
    fun updateUketEventRegistration(
        @PathVariable("uketEventRegistrationId") uketEventRegistrationId: Long,
        @PathVariable("eventType") eventType: EventType,
        @RequestBody request: RegisterUketEventRequest,
    ): RegisterUketEventResponse {
        request.validateByEventType(eventType)
        val originalEventRegistration = eventRegistrationService.getById(uketEventRegistrationId)

        val modifiedEventRegistrationId = modifyEventRegistrationFacade.modify(
            originalEventRegistration.id,
            request.toEntity(originalEventRegistration, eventType)
        )

        return RegisterUketEventResponse(
            uketEventRegistrationId = modifiedEventRegistrationId,
            eventType = eventType,
        )
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "내 행사 전체 조회", description = "내 행사 전체를 조회합니다.")
    @GetMapping("/admin/uket-event-registrations")
    fun getUketEventRegistrations(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @Parameter(hidden = true)
        @LoginAdminId adminId: Long,
    ): CustomPageResponse<UketEventRegistrationSummaryResponse> {
        val pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        val admin = adminService.getByIdWithOrganization(adminId)

        val registrations = if (admin.isSuperAdmin) {
            eventRegistrationService.findAll(pageRequest)
        } else {
            eventRegistrationService.findAllByOrganizationId(admin.organization.id, pageRequest)
        }

        val organizationMap = registrations
            .map { organizationService.getById(it.organizationId) }
            .associateBy { it.id }
        val summaryResponse = registrations.map { UketEventRegistrationSummaryResponse.of(it, organizationMap[it.organizationId]!!) }
        return CustomPageResponse(summaryResponse)
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "특정 행사 조회", description = "한 가지 특정 행사만 조회합니다.")
    @GetMapping("/admin/uket-event-registrations/{uketEventRegistrationId}")
    fun getUketEventRegistration(
        @PathVariable("uketEventRegistrationId") uketEventRegistrationId: Long,
    ): UketRegistrationEventResponse {
        val eventRegistration = eventRegistrationService.getByIdWithEventRoundAndEntryGroup(uketEventRegistrationId)
        return UketRegistrationEventResponse.from(eventRegistration)
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "행사 상태 변경", description = "해당 행사의 등록 상태를 변경합니다.")
    @PutMapping("/admin/uket-event-registrations/{uketEventRegistrationId}/status/{registrationStatus}")
    fun changeRegistrationStatus(
        @Parameter(hidden = true)
        @LoginAdminId adminId: Long,
        @PathVariable("uketEventRegistrationId") uketEventRegistrationId: Long,
        @PathVariable("registrationStatus") registrationStatusString: String,
    ): ChangeEventRegistrationStatusResponse {
        val admin = adminService.getById(adminId)
        check(admin.isSuperAdmin) { "슈퍼 어드민이 아닌데 호출되었습니다." }

        val eventRegistrationStatusState =
            eventRegistrationStatusStateResolver.resolve(registrationStatusString.toEnum())

        val eventRegistration = eventRegistrationStatusState.invoke(
            id = uketEventRegistrationId,
            currentStatus = eventRegistrationService.getById(uketEventRegistrationId).status
        )

        return ChangeEventRegistrationStatusResponse(
            uketEventRegistrationId = eventRegistration.id,
            currentStatus = eventRegistration.status
        )
    }
}
