package uket.uket.api.user.response

import uket.domain.uketevent.entity.UketEventRound
import java.time.LocalDateTime

data class EventRoundListItemResponse(
    val eventRoundId: Long,
    val eventRoundDateTime: LocalDateTime,
) {
    companion object {
        fun of(eventRound: UketEventRound): EventRoundListItemResponse = EventRoundListItemResponse(
            eventRoundId = eventRound.id,
            eventRoundDateTime = eventRound.eventRoundDateTime
        )
    }
}
