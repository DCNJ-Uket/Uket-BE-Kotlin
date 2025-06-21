package uket.api.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uket.api.user.request.TicketingRequest
import uket.api.user.response.TicketingResponse
import uket.auth.config.userId.LoginUserId
import uket.domain.payment.service.PaymentService
import uket.domain.reservation.service.QRService
import uket.domain.reservation.service.TicketService
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
    private val ticketService: TicketService,
    private val qRService: QRService,
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
        val tickets = ticketingFacade.ticketing(userId, request.entryGroupId, request.buyCount, request.performerName, now)
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

    @GetMapping("/tickets/{id}/qrcode")
    @Operation(summary = "티켓 QR 코드 발급", description = "티켓의 QR 코드를 발급할 수 있습니다.")
    fun getQRCode(
        @LoginUserId
        userId: Long,
        @PathVariable("id")
        ticketId: Long,
    ): ResponseEntity<ByteArray> {
        ticketService.validateTicketOwner(userId, ticketId)
        val qrCodeByte = qRService.generateTicketQRCode(ticketId)
        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(qrCodeByte)
    }
}
