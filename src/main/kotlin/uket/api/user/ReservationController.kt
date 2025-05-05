package uket.uket.api.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uket.auth.config.userId.LoginUserId
import uket.uket.api.user.request.TicketingRequest
import uket.uket.api.user.response.TicketingResponse
import uket.uket.facade.TicketingFacade

@Tag(name = "예매 관련 API", description = "예매 관련 API 입니다")
@RestController
class ReservationController(
    private val ticketingFacade: TicketingFacade,
) {
    @Operation(summary = "티켓 예매", description = "유저가 예매 가능한 그룹에 대한 티켓을 예매할 수 있습니다.")
    @PostMapping("/reservation")
    fun ticketing(
        @Parameter(hidden = true)
        @LoginUserId
        userId: Long,
        @RequestBody
        request: TicketingRequest,
    ): ResponseEntity<TicketingResponse> {
        val (tickets, event) = ticketingFacade.ticketing(userId, request.entryGroupId, request.ticketCount, request.friend)
        val paymentInfo = event.paymentInfo

        return ResponseEntity.ok(
            TicketingResponse(
                ticketIds = tickets.map { it.id },
                totalPrice = paymentInfo.ticketPrice * tickets.size,
                depositUrl = paymentInfo.depositUrl,
                bankCode = paymentInfo.bankCode
            )
        )
    }
}
