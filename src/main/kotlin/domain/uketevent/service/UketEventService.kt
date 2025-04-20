package uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.api.user.EventListQueryType
import uket.common.LoggerDelegate
import uket.common.enums.EventType
import uket.domain.uketevent.dto.EventListItem
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.enums.TicketingStatus
import uket.domain.uketevent.repository.UketEventRepository
import java.time.LocalDateTime

@Service
class UketEventService(
    private val uketEventRepository: UketEventRepository,
) {
    private val log by LoggerDelegate()

    @Transactional(readOnly = true)
    fun getById(uketEventId: Long): UketEvent {
        val uketEvent = uketEventRepository.findByIdOrNull(uketEventId)
            ?: throw IllegalStateException("해당 행사를 찾을 수 없습니다.")
        return uketEvent
    }

    @Transactional(readOnly = true)
    fun getActiveEventItemList(type: EventListQueryType): List<EventListItem> {
        val startTime = System.currentTimeMillis()
        lateinit var eventList: List<UketEvent>
        if (type.name.equals("ALL")) {
            eventList = uketEventRepository.findAllByEventEndDateBeforeNowWithUketEventRound()
        } else {
            eventList = uketEventRepository.findAllByEventTypeAndEventEndDateBeforeNowWithUketEventRound(EventType.valueOf(type.name))
        }

        val now = LocalDateTime.now()
        val itemList = eventList
            .map {
                var ticketingStatus = TicketingStatus.오픈_예정
                if (now.isAfter(it.ticketingStartDateTime)) {
                    ticketingStatus = TicketingStatus.티켓팅_진행중
                }
                if (now.isAfter(it.ticketingEndDateTime)) {
                    ticketingStatus = TicketingStatus.티켓팅_종료
                }

                EventListItem(
                    eventName = it.eventName,
                    eventThumbnailImagePath = it.thumbnailImageId,
                    eventStartDate = it.uketEventRounds.minOf { it.eventRoundDateTime },
                    eventEndDate = it.uketEventRounds.maxOf { it.eventRoundDateTime },
                    ticketingStartDate = it.ticketingStartDateTime,
                    ticketingEndDate = it.ticketingEndDateTime,
                    ticketingStatus = ticketingStatus
                )
            }
        val orderedList = itemList.sortedWith(
            compareBy<EventListItem> { it.ticketingStatus }
                .thenBy { it.eventStartDate }
        )
        val endTime = System.currentTimeMillis()
        log.info("[UketEventService.getActiveEventItemList] 메서드 실행 시간 : {}", endTime - startTime)
        return orderedList
    }

    @Transactional(readOnly = true)
    fun getOrganizationNameByUketEventIid(uketEventId: Long): String {
        val name = uketEventRepository.findOrganizationNameByUketEventId(uketEventId)
            ?: throw IllegalStateException("해당 행사의 이름을 찾을 수 없습니다.")
        return name
    }
}
