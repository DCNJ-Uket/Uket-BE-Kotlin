package uket.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import uket.api.admin.response.EnterUketEventResponse
import uket.api.admin.response.UpdateTicketStatusResponse
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.service.TicketService
import uket.facade.EnterUketEventFacade

@Tag(name = "어드민 티켓 관련 API", description = "어드민 티켓 관련 API 입니다.")
@RestController
@ApiResponse(responseCode = "200", description = "단체")
class AdminTicketController(
    private val enterUketEventFacade: EnterUketEventFacade,
    private val ticketService: TicketService,

) {
    @Operation(summary = "입장 확인 API", description = "QR code를 통한 Token값으로 입장 확인을 할 수 있습니다.")
    @PostMapping("/{token}/enter")
    fun enterShow(
        @PathVariable("token") ticketToken: String
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
}
