package uket.uket.api.user.response

import uket.domain.uketevent.entity.UketEventRound
import java.time.DayOfWeek
import java.time.LocalDateTime

data class EventRoundListItemResponse(
    val eventRoundId: Long,
    val eventRoundDateTime: LocalDateTime,
    val weekDay: DayOfWeek,
) {
    companion object {
        fun of(eventRound: UketEventRound): EventRoundListItemResponse = EventRoundListItemResponse(
            eventRoundId = eventRound.id,
            eventRoundDateTime = eventRound.eventRoundDateTime,
            weekDay = eventRound.eventRoundDateTime.dayOfWeek
        )
    }
}
