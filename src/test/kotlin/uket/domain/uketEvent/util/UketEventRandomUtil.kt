package uket.domain.uketEvent.util

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates.named
import uket.domain.uketevent.entity.Banner
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import java.time.LocalDateTime
import kotlin.random.Random

class UketEventRandomUtil {
    companion object {
        fun createUketEvent(
            uketEventRounds: List<UketEventRound>,
            banners: List<Banner> = listOf(),
            eventName: String = "name",
            id: Long = 0L,
        ): UketEvent {
            val easyRandom = EasyRandom(
                EasyRandomParameters()
                    .randomize(named("organizationId")) { 1L }
                    .randomize(named("id")) { id }
                    .randomize(named("eventName")) { eventName }
                    .randomize(named("banners")) {
                        banners.map {
                            Banner(
                                id = it.id,
                                uketEvent = null,
                                imageId = it.imageId,
                                link = it.link
                            )
                        }
                    }.randomize(named("uketEventRounds")) {
                        uketEventRounds.map {
                            UketEventRound(
                                id = it.id,
                                uketEvent = null,
                                eventRoundDateTime = it.eventRoundDateTime,
                                ticketingStartDateTime = it.ticketingStartDateTime,
                                ticketingEndDateTime = it.ticketingEndDateTime
                            )
                        }
                    }.randomize(named("firstRoundDateTime")) {
                        uketEventRounds.minOf { it.eventRoundDateTime }
                    }.randomize(named("lastRoundDateTime")) {
                        uketEventRounds.maxOf { it.eventRoundDateTime }
                    }.randomize(named("buyTicketLimit")) {
                        4
                    }.randomize(named("ticketPrice")) {
                        1000
                    }
            )
            val uketEvent = easyRandom.nextObject(UketEvent::class.java)
            uketEvent.uketEventRounds.forEach { it.uketEvent = uketEvent }
            uketEvent.banners.forEach { it.uketEvent = uketEvent }

            return uketEvent
        }

        fun createUketEventRounds(
            uketEventRoundDates: List<LocalDateTime>,
            ticketingStartDateTime: LocalDateTime,
            ticketingEndDateTime: LocalDateTime,
            startId: Long = 0L,
            startIdOption: Boolean = false,
        ): List<UketEventRound> {
            var id = startId
            val eventRounds = uketEventRoundDates.map {
                id += 1
                val easyRandomEventRound = EasyRandom(
                    EasyRandomParameters()
                        .randomize(named("id")) { if (startIdOption) id else 0L }
                        .randomize(named("uketEvent")) { null }
                        .randomize(named("eventRoundDateTime")) { it }
                        .randomize(named("ticketingStartDateTime")) { ticketingStartDateTime }
                        .randomize(named("ticketingEndDateTime")) { ticketingEndDateTime }
                )
                easyRandomEventRound.nextObject(UketEventRound::class.java)
            }

            return eventRounds
        }

        fun createEntryGroup(
            uketEventRound: UketEventRound,
            entryGroupStartTimes: List<LocalDateTime>,
        ): List<EntryGroup> {
            val entryGroups = entryGroupStartTimes.map {
                val easyRandomEntryGroup = EasyRandom(
                    EasyRandomParameters()
                        .randomize(named("id")) { 0L }
                        .randomize(named("entryStartDateTime")) { it }
                        .randomize(named("entryEndDateTime")) { it.plusMinutes(10) }
                        .randomize(named("ticketCount")) { 0 }
                        .randomize(named("totalTicketCount")) { Random.nextInt(3, 6) }
                )
                easyRandomEntryGroup.nextObject(EntryGroup::class.java)
            }

            entryGroups.forEach { it.uketEventRound = uketEventRound }

            return entryGroups
        }
    }
}
