package uket.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uket.api.admin.enums.TicketSearchType
import uket.api.admin.request.SearchRequest
import uket.api.admin.response.EnterUketEventResponse
import uket.api.admin.response.FilterEventResponse
import uket.api.admin.response.LiveEnterUserResponse
import uket.api.admin.response.TicketSearchResponse
import uket.api.admin.response.UpdateTicketStatusResponse
import uket.auth.config.adminId.LoginAdminId
import uket.common.PublicException
import uket.common.aop.ratelimit.LimitRequest
import uket.common.response.CustomPageResponse
import uket.domain.admin.service.AdminService
import uket.domain.reservation.dto.TicketSearchDto
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.reservation.service.search.TicketSearcher
import uket.domain.uketevent.service.UketEventService
import uket.facade.EnterUketEventFacade
import uket.facade.UpdateTicketStatusFacade

@Tag(name = "어드민 티켓 관련 API", description = "어드민 티켓 관련 API 입니다.")
@RestController
@RequestMapping("/admin")
@ApiResponse(responseCode = "200", description = "OK")
class AdminTicketController(
    private val enterUketEventFacade: EnterUketEventFacade,
    private val updateTicketStatusFacade: UpdateTicketStatusFacade,
    private val ticketService: TicketService,
    private val ticketSearchers: List<TicketSearcher>,
    private val adminService: AdminService,
    private val uketEventService: UketEventService,
) {
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "입장 확인 API", description = "QR code를 통한 Token값으로 입장 확인을 할 수 있습니다.")
    @PostMapping("/{token}/enter")
    fun enterShow(
        @PathVariable("token") ticketToken: String,
    ): ResponseEntity<EnterUketEventResponse> {
        val response: EnterUketEventResponse = enterUketEventFacade.enterUketEvent(ticketToken)
        return ResponseEntity.ok(response)
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "티켓 상태 변경 API", description = "어드민용 티켓 상태를 변경합니다.")
    @PatchMapping("/{ticketId}/status/{ticketStatus}")
    fun updateTicketStatus(
        @PathVariable("ticketId") ticketId: Long,
        @PathVariable("ticketStatus") ticketStatus: TicketStatus,
    ): ResponseEntity<UpdateTicketStatusResponse> {
        val ticket: Ticket = ticketService.getById(ticketId)
        val updatedTicket = updateTicketStatusFacade.updateTicketStatus(ticket.entryGroupId, ticketId, ticketStatus)

        val response = UpdateTicketStatusResponse.from(updatedTicket)
        return ResponseEntity.ok(response)
    }

    @LimitRequest
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "실시간 입장 내역 조회 API", description = "실시간 입장내역 조회를 합니다. 페이지는 1Page부터 시작합니다.")
    @GetMapping("/live/enter-users")
    fun searchLiveEnterUsers(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) uketEventId: Long?,
        @Parameter(hidden = true)
        @LoginAdminId adminId: Long,
    ): ResponseEntity<CustomPageResponse<LiveEnterUserResponse>> {
        val adminInfo = adminService.getAdminInfo(adminId)
        val response = enterUketEventFacade.searchLiveEnterUsers(
            adminInfo.organizationId,
            uketEventId,
            PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "updatedAt"))
        )
        return ResponseEntity.ok(CustomPageResponse(response))
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "티켓 검색 API", description = "다양한 기준으로 티켓을 페이지별로 조회합니다. 페이지는 1Page부터 시작합니다.")
    @GetMapping("/search")
    fun searchTickets(
        @RequestParam searchType: TicketSearchType,
        @ModelAttribute searchRequest: SearchRequest,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) uketEventId: Long?,
        @Parameter(hidden = true)
        @LoginAdminId adminId: Long,
    ): ResponseEntity<CustomPageResponse<TicketSearchResponse>> {
        val pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "updatedAt"))
        val ticketSearchType = searchType ?: TicketSearchType.default
        val adminInfo = adminService.getAdminInfo(adminId)

        val tickets: Page<TicketSearchDto> =
            if (ticketSearchType == TicketSearchType.NONE) {
                ticketService.searchAllTickets(adminInfo.organizationId, uketEventId, pageRequest)
            } else {
                ticketSearchers
                    .stream()
                    .filter { it.isSupport(ticketSearchType) }
                    .findFirst()
                    .orElseThrow {
                        throw PublicException(
                            publicMessage = "잘못된 검색 타입입니다.",
                            systemMessage = "Not Valid TicketSearchType : TICKETSEARCHTYPE =$ticketSearchType",
                            title = "잘못된 검색 타입"
                        )
                    }.search(adminInfo.organizationId, uketEventId, searchRequest, pageRequest);
            }

        val response = CustomPageResponse(TicketSearchResponse.from(tickets))
        return ResponseEntity.ok(response)
    }

    @SecurityRequirement(name = "JWT")
    @Operation(summary = "필터 행사 목록 조회 API", description = "예매 내역 및 실시간조회에서 내가 속해있는 단체가 가지고있는 행사 목록의 이름을 반환합니다.")
    @GetMapping("/filtering/events")
    fun getOrganizationFilterEvents(
        @Parameter(hidden = true)
        @LoginAdminId adminId: Long,
    ): ResponseEntity<FilterEventResponse> {
        val adminInfo = adminService.getAdminInfo(adminId)
        val eventList = uketEventService.getEventsByOrganizationId(adminInfo.organizationId)
        return ResponseEntity.ok(FilterEventResponse(eventList))
    }
}
