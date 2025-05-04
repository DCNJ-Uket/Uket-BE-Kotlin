package uket.api.user

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uket.api.user.request.EventListQueryType
import uket.api.user.response.ActiveEventsResponse
import uket.api.user.response.EventDetailResponse
import uket.common.response.ListResponse
import uket.domain.uketevent.service.EntryGroupService
import uket.domain.uketevent.service.UketEventRoundService
import uket.domain.uketevent.service.UketEventService
import uket.uket.api.user.response.EntryGroupListItemResponse
import uket.uket.api.user.response.EventRoundListItemResponse
import uket.uket.api.user.response.ReservationInfoResponse
import uket.uket.facade.UketEventFacade
import java.time.LocalDateTime

@Tag(name = "행사 관련 API", description = "행사 관련 API 입니다")
@RestController
class EventController(
    private val uketEventService: UketEventService,
    private val entryGroupService: EntryGroupService,
    private val uketEventRoundService: UketEventRoundService,
    private val uketEventFacade: UketEventFacade,
) {
    @Operation(summary = "활성화된 행사 목록 조회", description = "누구나 조회 가능한 행사 목록을 가져옵니다")
    @GetMapping("/uket-events")
    fun getEvents(
        @RequestParam("type") type: EventListQueryType,
    ): ResponseEntity<ActiveEventsResponse> {
        val itemList = uketEventService.getActiveEventItemList(type = type.name)
        return ResponseEntity.ok(ActiveEventsResponse(itemList))
    }

    @Operation(summary = "행사 상세 정보 조회", description = "누구나 조회 가능한 행사의 상세 정보를 가져옵니다")
    @GetMapping("/uket-events/{id}")
    fun getEvents(
        @PathVariable("id") eventId: Long,
    ): ResponseEntity<EventDetailResponse> {
        val event = uketEventService.getDetailById(eventId)
        return ResponseEntity.ok(EventDetailResponse.from(event))
    }

    @Operation(summary = "행사 회차 목록 조회", description = "유저가 예매 가능한 행사의 현재 날짜와 이후 회차 목록을 가져옵니다")
    @GetMapping("/uket-events/{id}/rounds")
    fun getEventRounds(
        @PathVariable("id") eventId: Long,
    ): ResponseEntity<ListResponse<EventRoundListItemResponse>> {
        val now = LocalDateTime.now()

        val event = uketEventService.getById(eventId)
        event.validateNowTicketing(now)

        val eventRounds = uketEventRoundService.findByUketEventIdAndDateAfter(eventId, now)
        val responses = eventRounds.map { EventRoundListItemResponse.of(it) }
        return ResponseEntity.ok(ListResponse(responses))
    }

    @Operation(summary = "회차 입장 그룹 목록 조회", description = "유저가 예매 가능한 회차의 입장 그룹 목록을 가져옵니다")
    @GetMapping("/rounds/{id}/entry-groups")
    fun getEntryGroups(
        @PathVariable("id") eventRoundId: Long,
    ): ResponseEntity<ListResponse<EntryGroupListItemResponse>> {
        val now = LocalDateTime.now()

        val uketEventRound = uketEventRoundService.getByIdWithUketEvent(eventRoundId)
        val uketEvent = uketEventRound.uketEvent!!
        uketEvent.validateNowTicketing(now)

        val entryGroups = entryGroupService.findValidByUketEventRoundIdAfter(eventRoundId, now)
        val responses = entryGroups.map { EntryGroupListItemResponse.of(it) }
        return ResponseEntity.ok(ListResponse(responses))
    }

    @Operation(summary = "예매 관련 정보 조회", description = "유저가 예매 관련 정보를 가져옵니다")
    @GetMapping("/uket-events/{id}/reservation")
    fun getReservationInfo(
        @PathVariable("id") eventId: Long,
    ): ResponseEntity<ReservationInfoResponse> {
        val now = LocalDateTime.now()

        val event = uketEventService.getById(eventId)
        event.validateNowTicketing(now)

        val entryGroupMap = uketEventFacade.findValidEntryGroupMap(eventId, now)
        val roundResponses = entryGroupMap.keys.map { round ->
            val groups = entryGroupMap.get(round)
            val groupResponses = groups!!.map {
                EntryGroupListItemResponse.of(it)
            }
            ReservationInfoResponse.EventRoundWithGroupResponse.of(round, groupResponses)
        }

        val response = ReservationInfoResponse.of(event, roundResponses)
        return ResponseEntity.ok(response)
    }
}
