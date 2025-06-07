package uket.api.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uket.api.user.request.TicketingRequest
import uket.api.user.response.TicketingResponse
import uket.auth.config.userId.LoginUserId
import uket.domain.payment.service.PaymentService
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.uketevent.service.UketEventService
import uket.facade.TicketingFacade
import java.time.LocalDateTime

@Tag(name = "티켓 관련 API", description = "티켓 관련 API 입니다")
@RestController
class TicketController(
    private val ticketingFacade: TicketingFacade,
    private val paymentService: PaymentService,
    private val uketEventService: UketEventService,
    private val entryGroupService: EntryGroupService,
) {
    @Operation(summary = "티켓 예매", description = "유저가 예매 가능한 그룹에 대한 티켓을 예매할 수 있습니다.")
    @PostMapping("/tickets")
    fun ticketing(
        @Parameter(hidden = true)
        @LoginUserId
        userId: Long,
        @RequestBody
        request: TicketingRequest,
    ): ResponseEntity<TicketingResponse> {
        val now = LocalDateTime.now()
        val tickets = ticketingFacade.ticketing(userId, request.entryGroupId, request.buyCount, request.friend, now)
        val entryGroup = entryGroupService.getById(request.entryGroupId)
        val event = uketEventService.getById(entryGroup.uketEventId)
        val payment = paymentService.getByOrganizationId(event.organizationId)

        return ResponseEntity.ok(
            TicketingResponse(
                ticketIds = tickets.map { it.id },
                totalPrice = event.ticketPrice * tickets.size,
                depositUrl = payment.depositLink,
                bankCode = payment.account.bankCode
            )
        )
    }
}
