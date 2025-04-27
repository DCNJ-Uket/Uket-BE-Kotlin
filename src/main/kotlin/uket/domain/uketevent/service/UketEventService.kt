package uket.domain.uketevent.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.api.admin.dto.EventNameDto
import uket.common.LoggerDelegate
import uket.common.PublicException
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
    fun validateNowTicketing(uketEvent: UketEvent) {
        val ticketingStatus = uketEvent.getTicketingStatus(LocalDateTime.now())
        if (ticketingStatus != TicketingStatus.티켓팅_진행중) {
            throw PublicException(
                publicMessage = "예매가 불가능 한 행사입니다",
                systemMessage = "[UketEventService] 티켓팅이 진행 중이지 않은 행사 validation",
                title = "예매 불가능 한 행사"
            )
        }
    }

    @Transactional(readOnly = true)
    fun getDetailById(uketEventId: Long): UketEvent {
        val uketEvent = uketEventRepository.findByIdAndLastRoundDateAfterNowWithBanners(uketEventId)
            ?: throw IllegalStateException("해당 행사를 찾을 수 없습니다.")
        return uketEvent
    }

    @Transactional(readOnly = true)
    fun getActiveEventItemList(type: String): List<EventListItem> {
        val startTime = System.currentTimeMillis()

        lateinit var eventList: List<UketEvent>
        if (type == "ALL") {
            eventList = uketEventRepository.findAllByEventEndDateAfterNowWithUketEventRound()
        } else {
            eventList = uketEventRepository.findAllByEventTypeAndEventEndDateAfterNowWithUketEventRound(EventType.valueOf(type))
        }

        val now = LocalDateTime.now()
        val itemList = eventList
            .map {
                val ticketingStatus = it.getTicketingStatus(now)
                EventListItem.of(it, ticketingStatus)
            }
        val orderedList = itemList.sortedWith(
            compareBy<EventListItem> { it.ticketingStatus }
                .thenBy { it.eventStartDate }
        )

        val endTime = System.currentTimeMillis()
        log.debug("[UketEventService.getActiveEventItemList] 메서드 실행 시간 : {}", endTime - startTime)
        return orderedList
    }

    @Transactional(readOnly = true)
    fun getOrganizationNameByUketEventIid(uketEventId: Long): String {
        val name = uketEventRepository.findOrganizationNameByUketEventId(uketEventId)
            ?: throw IllegalStateException("해당 행사의 이름을 찾을 수 없습니다.")
        return name
    }

    @Transactional(readOnly = true)
    fun getEventsByOrganizationId(organizationId: Long): List<EventNameDto> {
        val events = uketEventRepository.findAllByOrganizationId(organizationId)
        return events.map { event -> EventNameDto.of(organizationId, event) }
    }
}
