package uket.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uket.api.admin.enums.TicketSearchType
import uket.api.admin.request.SearchRequest
import uket.api.admin.response.EnterUketEventResponse
import uket.api.admin.response.LiveEnterUserResponse
import uket.api.admin.response.TicketSearchResponse
import uket.api.admin.response.UpdateTicketStatusResponse
import uket.common.PublicException
import uket.common.aop.ratelimit.LimitRequest
import uket.common.response.CustomPageResponse
import uket.domain.reservation.dto.TicketSearchDto
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.domain.reservation.service.search.TicketSearcher
import uket.facade.EnterUketEventFacade

@Tag(name = "어드민 티켓 관련 API", description = "어드민 티켓 관련 API 입니다.")
@RestController
@ApiResponse(responseCode = "200", description = "OK")
class AdminTicketController(
    private val enterUketEventFacade: EnterUketEventFacade,
    private val ticketService: TicketService,
    private val ticketSearchers: List<TicketSearcher>,

) {
    @Operation(summary = "입장 확인 API", description = "QR code를 통한 Token값으로 입장 확인을 할 수 있습니다.")
    @PostMapping("/{token}/enter")
    fun enterShow(
        @PathVariable("token") ticketToken: String,
    ): ResponseEntity<EnterUketEventResponse> {
        val response: EnterUketEventResponse = enterUketEventFacade.enterUketEvent(ticketToken)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "티켓 상태 변경 API", description = "어드민용 티켓 상태를 변경합니다.")
    @PatchMapping("/{ticketId}/status/{ticketStatus}")
    fun updateTicketStatus(
        @PathVariable("ticketId") ticketId: Long,
        @PathVariable("ticketStatus") ticketStatus: TicketStatus,
    ): ResponseEntity<UpdateTicketStatusResponse> {
        val ticket: Ticket = ticketService.updateTicketStatus(ticketId, ticketStatus)
        val response = UpdateTicketStatusResponse.from(ticket)
        return ResponseEntity.ok(response)
    }

    @LimitRequest
    @Operation(summary = "실시간 입장 내역 조회 API", description = "실시간 입장내역 조회를 합니다. 페이지는 1Page부터 시작합니다.")
    @GetMapping("/live/enter-users/{uketEventId}")
    fun searchLiveEnterUsers(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @PathVariable("uketEventId") uketEventId: Long,
    ): ResponseEntity<CustomPageResponse<LiveEnterUserResponse>> {
        val response = enterUketEventFacade.searchLiveEnterUsers(
            uketEventId,
            PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "modifiedAt"))
        )
        return ResponseEntity.ok(CustomPageResponse(response))
    }

    @Operation(summary = "티켓 검색 API", description = "다양한 기준으로 티켓을 페이지별로 조회합니다. 페이지는 1Page부터 시작합니다.")
    @GetMapping("/search/{uketEventId}")
    fun searchTickets(
        @RequestParam searchType: TicketSearchType,
        @ModelAttribute searchRequest: SearchRequest,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @PathVariable("uketEventId") uketEventId: Long,
    ): ResponseEntity<CustomPageResponse<TicketSearchResponse>> {
        val pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "modifiedAt"))
        val ticketSearchType = searchType ?: TicketSearchType.default

        val tickets: Page<TicketSearchDto> =
            if (ticketSearchType == TicketSearchType.NONE) {
                ticketService.searchAllTickets(uketEventId, pageRequest)
            } else {
                ticketSearchers.stream()
                    .filter { it.isSupport(ticketSearchType) }
                    .findFirst().orElseThrow {
                        throw PublicException(
                            publicMessage = "잘못된 검색 타입입니다.",
                            systemMessage = "Not Valid TicketSearchType : TICKETSEARCHTYPE =$ticketSearchType",
                            title = "잘못된 검색 타입"
                        )
                    }
                    .search(uketEventId, searchRequest, pageRequest);
            }

        val response = CustomPageResponse(TicketSearchResponse.from(tickets))
        return ResponseEntity.ok(response)
    }
}
