package uket.api.user.response

import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import java.time.DayOfWeek
import java.time.LocalDateTime

data class ReservationInfoResponse(
    val eventRounds: List<EventRoundWithGroupResponse>,
    val ticketPrice: Long,
    val buyTicketLimit: Int,
    val friend: String,
) {
    data class EventRoundWithGroupResponse(
        val eventRoundId: Long,
        val eventRoundDateTime: LocalDateTime,
        val weekDay: DayOfWeek,
        val entryGroups: List<EntryGroupListItemResponse>,
    ) {
        companion object {
            fun of(
                uketEventRound: UketEventRound,
                entryGroups: List<EntryGroupListItemResponse>,
            ): EventRoundWithGroupResponse = EventRoundWithGroupResponse(
                eventRoundId = uketEventRound.id,
                eventRoundDateTime = uketEventRound.eventRoundDateTime,
                weekDay = uketEventRound.eventRoundDateTime.dayOfWeek,
                entryGroups = entryGroups
            )
        }
    }

    companion object {
        fun of(
            uketEvent: UketEvent,
            roundResponses: List<EventRoundWithGroupResponse>,
        ): ReservationInfoResponse = ReservationInfoResponse(
            eventRounds = roundResponses,
            ticketPrice = uketEvent.ticketPrice,
            buyTicketLimit = uketEvent.buyTicketLimit,
            friend = ""
        )
    }
}
