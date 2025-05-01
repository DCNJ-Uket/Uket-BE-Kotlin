package uket.domain.uketEvent

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates.named
import uket.domain.uketevent.entity.Banner
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import java.time.LocalDateTime
import kotlin.random.Random

class UketEventRandomUtil {
    companion object {
        fun createUketEventWithDatesAndEventRoundsAndBanners(
            ticketingStartDateTime: LocalDateTime,
            ticketingEndDateTime: LocalDateTime,
            uketEventRounds: List<UketEventRound>,
            banners: List<Banner>,
        ): UketEvent {
            val easyRandom = EasyRandom(
                EasyRandomParameters()
                    .randomize(named("id")) {
                        0L
                    }.randomize(named("ticketingStartDateTime")) { ticketingStartDateTime }
                    .randomize(named("ticketingEndDateTime")) { ticketingEndDateTime }
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
                                eventRoundDateTime = it.eventRoundDateTime
                            )
                        }
                    }.randomize(named("firstRoundDateTime")) {
                        uketEventRounds.minOf { it.eventRoundDateTime }
                    }.randomize(named("lastRoundDateTime")) {
                        uketEventRounds.maxOf { it.eventRoundDateTime }
                    }
            )
            val uketEvent = easyRandom.nextObject(UketEvent::class.java)
            uketEvent.uketEventRounds.forEach { it.uketEvent = uketEvent }
            uketEvent.banners.forEach { it.uketEvent = uketEvent }

            return uketEvent
        }

        fun createUketEventWithDatesAndNameAndIdAndEventRounds(
            ticketingStartDateTime: LocalDateTime,
            ticketingEndDateTime: LocalDateTime,
            eventName: String,
            id: Long,
            uketEventRounds: List<UketEventRound>,
        ): UketEvent {
            val easyRandom = EasyRandom(
                EasyRandomParameters()
                    .randomize(named("id")) {
                        id
                    }.randomize(named("ticketingStartDateTime")) { ticketingStartDateTime }
                    .randomize(named("ticketingEndDateTime")) { ticketingEndDateTime }
                    .randomize(named("eventName")) {
                        eventName
                    }.randomize(named("banners")) {
                        listOf<Banner>()
                    }.randomize(named("uketEventRounds")) {
                        uketEventRounds.map {
                            UketEventRound(
                                id = it.id,
                                uketEvent = null,
                                eventRoundDateTime = it.eventRoundDateTime
                            )
                        }
                    }.randomize(named("firstRoundDateTime")) {
                        uketEventRounds.minOf { it.eventRoundDateTime }
                    }.randomize(named("lastRoundDateTime")) {
                        uketEventRounds.maxOf { it.eventRoundDateTime }
                    }
            )
            val uketEvent = easyRandom.nextObject(UketEvent::class.java)
            uketEvent.uketEventRounds.forEach { it.uketEvent = uketEvent }

            return uketEvent
        }

        fun createUketEventsRoundWithDate(
            uketEventRoundDates: List<LocalDateTime>,
        ): List<UketEventRound> {
            val eventRounds = uketEventRoundDates.map {
                val easyRandomEventRound = EasyRandom(
                    EasyRandomParameters()
                        .randomize(named("id")) { 0L }
                        .randomize(named("uketEvent")) { null }
                        .randomize(named("eventRoundDateTime")) { it }
                )
                easyRandomEventRound.nextObject(UketEventRound::class.java)
            }

            return eventRounds
        }

        fun createDummyData() {
            val now = LocalDateTime.now()
            val events = mutableListOf<UketEvent>()
            val rounds = mutableListOf<UketEventRound>()

            repeat(100) { i ->
                val ticketingStart = now.plusDays(Random.nextLong(-10, 10))
                val ticketingEnd = now.plusDays(Random.nextLong(0, 3))

                val roundDate = ticketingEnd.plusDays(Random.nextLong(1, 30))
                val roundSize = Random.nextInt(1, 3)
                val roundDateTimeList = mutableListOf<LocalDateTime>()
                repeat(roundSize) { s ->
                    roundDateTimeList.add(roundDate.plusDays(s.toLong()))
                }
                val uketEventRounds = createUketEventsRoundWithDate(roundDateTimeList)

                val event = createUketEventWithDatesAndNameAndIdAndEventRounds(
                    ticketingStart,
                    ticketingEnd,
                    "행사$i",
                    i.toLong() + 1,
                    uketEventRounds
                )

                events.add(event)
                rounds.addAll(event.uketEventRounds)
            }

            println("-- Insert Events")
            events.forEach {
                println(toInsertSqlForUketEvent(it))
            }

            println("-- Insert Event Rounds")
            rounds.forEach {
                println(toInsertSqlForUketEventRound(it))
            }
        }

        fun toInsertSqlForUketEventRound(uketEventRound: UketEventRound): String {
            val tableName = "uket_event_round"

            val columns = mutableListOf<String>()
            val values = mutableListOf<String>()

            // 직접 필드 접근 (중첩 필드도 분해해서 처리)
            with(uketEventRound) {
                // 일반 필드
                columns += listOf(
                    "uket_event_id",
                    "event_round_datetime",
                    "created_at",
                    "updated_at"
                )

                values += listOf(
                    uketEvent!!.id,
                    eventRoundDateTime,
                    createdAt,
                    updatedAt
                ).map { toSqlValue(it) }
            }

            return "INSERT INTO $tableName (${columns.joinToString(", ")}) VALUES (${values.joinToString(", ")});"
        }

        fun toInsertSqlForUketEvent(event: UketEvent): String {
            val tableName = "uket_event"

            val columns = mutableListOf<String>()
            val values = mutableListOf<String>()

            // 직접 필드 접근 (중첩 필드도 분해해서 처리)
            with(event) {
                // 일반 필드
                columns += listOf(
                    "organization_id",
                    "event_name",
                    "event_type",
                    "location",
                    "ticketing_start_datetime",
                    "ticketing_end_datetime",
                    "ticket_price",
                    "total_ticket_count",
                    "event_image_id",
                    "thumbnail_image_id",
                    "first_round_datetime",
                    "last_round_datetime",
                    "created_at",
                    "updated_at"
                )

                values += listOf(
                    organizationId,
                    eventName,
                    eventType.name,
                    location,
                    ticketingStartDateTime,
                    ticketingEndDateTime,
                    ticketPrice,
                    totalTicketCount,
                    eventImageId,
                    thumbnailImageId,
                    firstRoundDateTime,
                    lastRoundDateTime,
                    createdAt,
                    updatedAt
                ).map { toSqlValue(it) }

                // Embedded: EventDetails -> information, caution, contact_type, contact_content
                columns += listOf("information", "caution", "contact_type", "contact_content")
                values += listOf(
                    details.information,
                    details.caution,
                    details.contact.type.name,
                    details.contact.content
                ).map { toSqlValue(it) }
            }

            return "INSERT INTO $tableName (${columns.joinToString(", ")}) VALUES (${values.joinToString(", ")});"
        }

        fun toSqlValue(value: Any?): String = when (value) {
            null -> "NULL"
            is String -> "'${value.replace("'", "''")}'"
            is Enum<*> -> "'${value.name}'"
            is LocalDateTime -> "'$value'"
            is Boolean -> if (value) "1" else "0"
            else -> value.toString()
        }
    }
}
