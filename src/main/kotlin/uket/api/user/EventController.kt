package uket.api.user

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uket.api.user.request.EventListQueryType
import uket.api.user.response.ActiveEventsResponse
import uket.api.user.response.EventDetailResponse
import uket.domain.uketevent.service.UketEventService

@RestController
class EventController(
    val uketEventService: UketEventService,
) {
    @Operation(summary = "활성화된 행사 목록 조회", description = "유저가 조회 가능한 행사 목록을 가져옵니다")
    @GetMapping("/uket-events")
    fun getEvents(
        @RequestParam("type") type: EventListQueryType,
    ): ResponseEntity<ActiveEventsResponse> {
        val itemList = uketEventService.getActiveEventItemList(type = type)
        return ResponseEntity.ok(ActiveEventsResponse(itemList))
    }

    @Operation(summary = "행사 상세 정보 조회", description = "유저가 조회 가능한 행사의 상세 정보를 가져옵니다")
    @GetMapping("/uket-events/{id}")
    fun getEvents(
        @PathVariable("id") eventId: Long,
    ): ResponseEntity<EventDetailResponse> {
        val response = uketEventService.getDetailById(eventId)
        return ResponseEntity.ok(response)
    }
}
