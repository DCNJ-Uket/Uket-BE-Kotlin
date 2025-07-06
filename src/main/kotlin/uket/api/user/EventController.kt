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
import uket.api.user.response.EntryGroupListItemResponse
import uket.api.user.response.EventDetailResponse
import uket.api.user.response.EventRoundListItemResponse
import uket.api.user.response.PerformerResponse
import uket.api.user.response.ReservationInfoResponse
import uket.common.response.ListResponse
import uket.domain.admin.service.OrganizationService
import uket.domain.uketevent.service.BannerService
import uket.domain.uketevent.service.PerformerService
import uket.domain.uketevent.service.UketEventRoundService
import uket.domain.uketevent.service.UketEventService
import uket.facade.UketEventFacade
import java.time.LocalDateTime

@Tag(name = "행사 관련 API", description = "행사 관련 API 입니다")
@RestController
class EventController(
    private val uketEventService: UketEventService,
    private val uketEventRoundService: UketEventRoundService,
    private val uketEventFacade: UketEventFacade,
    private val organizationService: OrganizationService,
    private val bannerService: BannerService,
    private val performerService: PerformerService,
) {
    @Operation(summary = "활성화된 행사 목록 조회", description = "누구나 조회 가능한 행사 목록을 가져옵니다")
    @GetMapping("/uket-events")
    fun getEvents(
        @RequestParam("type") type: EventListQueryType,
    ): ResponseEntity<ActiveEventsResponse> {
        val now = LocalDateTime.now()
        val itemList = uketEventFacade.getNowActiveEventItemList(type.name, now)
        return ResponseEntity.ok(ActiveEventsResponse(itemList))
    }

    @Operation(summary = "행사 상세 정보 조회", description = "누구나 조회 가능한 행사의 상세 정보를 가져옵니다")
    @GetMapping("/uket-events/{id}")
    fun getEvents(
        @PathVariable("id") eventId: Long,
    ): ResponseEntity<EventDetailResponse> {
        val event = uketEventService.getDetailById(eventId)
        val rounds = uketEventRoundService.getEventRoundsByEventId(eventId)
        check(rounds.size > 0) {
            throw IllegalStateException("해당 행사의 회차가 없습니다.")
        }
        val organization = organizationService.getById(event.organizationId)
        val banners = bannerService.findAllByUketEventId(event.id)
        val ticketingStatus = uketEventFacade.getCurrentEventTicketingStatus(eventId, LocalDateTime.now())

        return ResponseEntity.ok(
            EventDetailResponse.of(
                uketEvent = event,
                uketEventRound = rounds.get(0),
                organization = organization,
                banners = banners,
                ticketingStatus = ticketingStatus
            )
        )
    }

    @Operation(summary = "행사 회차 목록 조회", description = "유저가 티켓팅 중인 행사의 현재 날짜와 이후 회차 목록을 가져옵니다")
    @GetMapping("/uket-events/{id}/rounds")
    fun getEventRounds(
        @PathVariable("id") eventId: Long,
    ): ResponseEntity<ListResponse<EventRoundListItemResponse>> {
        val now = LocalDateTime.now()
        val eventRounds = uketEventRoundService.getNowTicketingRounds(eventId, now)

        val responses = eventRounds.map { EventRoundListItemResponse.of(it) }
        return ResponseEntity.ok(ListResponse(responses))
    }

    @Operation(summary = "회차 입장 그룹 목록 조회", description = "유저가 티켓팅 중인 행사의 특정 회차에 속한 입장 그룹 목록을 가져옵니다")
    @GetMapping("/rounds/{id}/entry-groups")
    fun getEntryGroups(
        @PathVariable("id") eventRoundId: Long,
    ): ResponseEntity<ListResponse<EntryGroupListItemResponse>> {
        val now = LocalDateTime.now()
        val entryGroups = uketEventFacade.findAllValidEntryGroup(eventRoundId, now)

        val responses = entryGroups.map { EntryGroupListItemResponse.of(it) }
        return ResponseEntity.ok(ListResponse(responses))
    }

    @Operation(summary = "예매 관련 정보 조회", description = "유저가 티켓팅 중인 행사의 예매 관련 정보를 가져옵니다")
    @GetMapping("/uket-events/{id}/reservation")
    fun getReservationInfo(
        @PathVariable("id") eventId: Long,
    ): ResponseEntity<ReservationInfoResponse> {
        val now = LocalDateTime.now()
        val entryGroupMap = uketEventFacade.getValidEntryGroupMap(eventId, now)
        val roundResponses = entryGroupMap.keys.map { round ->
            val groups = entryGroupMap.get(round)
            val groupResponses = groups!!.map {
                EntryGroupListItemResponse.of(it)
            }
            ReservationInfoResponse.EventRoundWithGroupResponse.of(round, groupResponses)
        }

        val uketEvent = uketEventService.getById(eventId)

        val response = ReservationInfoResponse.of(uketEvent, roundResponses)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "회차 출연자 목록 조회", description = "회차의 공연자 목록을 조회합니다.")
    @GetMapping("/rounds/{id}/performers")
    fun getPerformers(
        @PathVariable("id") uketEventRoundId: Long,
    ): ResponseEntity<ListResponse<PerformerResponse>> {
        val performers = performerService.findAllByUketEventRoundId(uketEventRoundId)
        val responses = performers.map { PerformerResponse.from(it) }
        return ResponseEntity.ok(ListResponse(responses))
    }
}
