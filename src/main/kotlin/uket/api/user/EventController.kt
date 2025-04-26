package uket.api.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uket.api.user.request.EventListQueryType
import uket.api.user.response.ActiveEventsResponse
import uket.domain.uketevent.service.UketEventService

@Tag(name = "행사 관련 API", description = "행사 관련 API 입니다")
@RestController
class EventController(
    val uketEventService: UketEventService,
) {
    @Operation(summary = "활성화된 축제 목록 조회", description = "유저가 조회 가능한 축제 목록을 가져옵니다")
    @GetMapping("/uket-events")
    fun getEvents(
        @RequestParam("type") type: EventListQueryType,
    ): ResponseEntity<ActiveEventsResponse> {
        val itemList = uketEventService.getActiveEventItemList(type = type)
        return ResponseEntity.ok(ActiveEventsResponse(itemList))
    }
}
