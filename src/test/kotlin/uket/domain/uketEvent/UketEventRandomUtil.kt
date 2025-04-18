package uket.domain.uketEvent

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates.named
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import java.time.LocalDateTime

class UketEventRandomUtil {
    companion object {
        fun createUketEventWithDates(ticketingStartDateTime: LocalDateTime, ticketingEndDateTime: LocalDateTime): UketEvent {
            val easyRandom = EasyRandom(
                EasyRandomParameters()
                    .randomize(named("id")) {
                        0L
                    }.randomize(named("ticketingStartDateTime")) { ticketingStartDateTime }
                    .randomize(named("ticketingEndDateTime")) { ticketingEndDateTime }
                    .randomize(named("uketEventRounds")) { listOf<UketEvent>() }
            )
            val uketEvent = easyRandom.nextObject(UketEvent::class.java)

            return uketEvent
        }

        fun createUketEventsRoundWithDate(uketEvent: UketEvent, uketEventRoundDates: List<LocalDateTime>): List<UketEventRound> {
            val eventRounds = uketEventRoundDates.map {
                val easyRandomEventRound = EasyRandom(
                    EasyRandomParameters()
                        .randomize(named("id")) { 0L }
                        .randomize(named("eventRoundDateTime")) { it }
                )
                easyRandomEventRound.nextObject(UketEventRound::class.java)
            }

            eventRounds.forEach { uketEvent.addUketEventRound(it) }
            println("uketEventRounds : ${uketEvent.uketEventRounds}")

            return eventRounds
        }
    }
}
