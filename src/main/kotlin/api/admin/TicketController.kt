package uket.api.admin

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import uket.api.admin.response.EnterUketEventResponse
import uket.facade.EnterUketEventFacade

@Tag(name = "어드민 티켓 관련 API", description = "어드민 티켓 관련 API 입니다.")
@RestController
@ApiResponse(responseCode = "200", description = "단체")
class TicketController(
    private val enterUketEventFacade: EnterUketEventFacade,

) {
    @Operation(summary = "입장 확인 API", description = "QR code를 통한 Token값으로 입장 확인을 할 수 있습니다.")
    @PostMapping("/{token}/enter")
    fun enterShow(
        @PathVariable("token") ticketToken: String
    ): ResponseEntity<EnterUketEventResponse> {
        val response: EnterUketEventResponse = enterUketEventFacade.enterUketEvent(ticketToken)
        return ResponseEntity.ok(response)
    }
}
