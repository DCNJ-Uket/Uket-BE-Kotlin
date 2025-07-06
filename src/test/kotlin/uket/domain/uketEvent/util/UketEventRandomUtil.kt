package uket.domain.uketEvent.util

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates.named
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import java.time.LocalDateTime
import kotlin.random.Random

class UketEventRandomUtil {
    companion object {
        fun createUketEvent(
            eventName: String = "name",
            id: Long = 0L,
        ): UketEvent {
            val easyRandom = EasyRandom(
                EasyRandomParameters()
                    .randomize(named("organizationId")) { 1L }
                    .randomize(named("id")) { id }
                    .randomize(named("eventName")) { eventName }
                    .randomize(named("buyTicketLimit")) {
                        4
                    }.randomize(named("ticketPrice")) {
                        1000
                    }
            )
            val uketEvent = easyRandom.nextObject(UketEvent::class.java)

            return uketEvent
        }

        fun createUketEventRound(
            uketEvent: UketEvent,
            uketEventRoundDate: LocalDateTime,
            ticketingStartDateTime: LocalDateTime,
            ticketingEndDateTime: LocalDateTime,
            id: Long = 0L,
        ): UketEventRound {
            val easyRandomEventRound = EasyRandom(
                EasyRandomParameters()
                    .randomize(named("id")) { id }
                    .randomize(named("uketEventId")) { uketEvent.id }
                    .randomize(named("eventRoundDateTime")) { uketEventRoundDate }
                    .randomize(named("ticketingStartDateTime")) { ticketingStartDateTime }
                    .randomize(named("ticketingEndDateTime")) { ticketingEndDateTime }
            )
            return easyRandomEventRound.nextObject(UketEventRound::class.java)
        }

        fun createEntryGroup(
            uketEventRound: UketEventRound,
            entryGroupStartTime: LocalDateTime,
            entryGroupEndTime: LocalDateTime = entryGroupStartTime.plusMinutes(10),
            totalTicketCount: Int = Random.nextInt(3, 6),
            id: Long = 0L,
            eventId: Long = 0L,
        ): EntryGroup {
            val easyRandomEntryGroup = EasyRandom(
                EasyRandomParameters()
                    .randomize(named("id")) { id }
                    .randomize(named("uketEventRound")) { uketEventRound }
                    .randomize(named("uketEventId")) { eventId }
                    .randomize(named("entryStartDateTime")) { entryGroupStartTime }
                    .randomize(named("entryEndDateTime")) { entryGroupEndTime }
                    .randomize(named("ticketCount")) { 0 }
                    .randomize(named("totalTicketCount")) { totalTicketCount }
            )
            return easyRandomEntryGroup.nextObject(EntryGroup::class.java)
        }
    }
}
