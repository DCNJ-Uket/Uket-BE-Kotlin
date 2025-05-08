package uket.domain.uketEvent

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates.named
import uket.domain.payment.entity.Payment
import uket.domain.uketevent.entity.Banner
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import java.io.File
import java.time.LocalDateTime
import kotlin.random.Random

class UketEventRandomUtil {
    companion object {
        fun createUketEvent(
            uketEventRounds: List<UketEventRound>,
            banners: List<Banner>,
        ): UketEvent {
            val easyRandom = EasyRandom(
                EasyRandomParameters()
                    .randomize(named("id")) { 0L }
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
                    }
            )
            val uketEvent = easyRandom.nextObject(UketEvent::class.java)
            uketEvent.uketEventRounds.forEach { it.uketEvent = uketEvent }
            uketEvent.banners.forEach { it.uketEvent = uketEvent }

            return uketEvent
        }

        fun createUketEventsRoundsWithDate(
            uketEventRoundDates: List<LocalDateTime>,
            ticketingStartDateTime: LocalDateTime,
            ticketingEndDateTime: LocalDateTime,
        ): List<UketEventRound> {
            val eventRounds = uketEventRoundDates.map {
                val easyRandomEventRound = EasyRandom(
                    EasyRandomParameters()
                        .randomize(named("id")) { 0L }
                        .randomize(named("uketEvent")) { null }
                        .randomize(named("eventRoundDateTime")) { it }
                        .randomize(named("ticketingStartDateTime")) { ticketingStartDateTime }
                        .randomize(named("ticketingEndDateTime")) { ticketingEndDateTime }
                )
                easyRandomEventRound.nextObject(UketEventRound::class.java)
            }

            return eventRounds
        }

        fun createEntryGroupWithTime(
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
                        .randomize(named("totalTicketCount")) { 10 }
                )
                easyRandomEntryGroup.nextObject(EntryGroup::class.java)
            }

            entryGroups.forEach { it.uketEventRound = uketEventRound }

            return entryGroups
        }

        fun createDummyData(count: Int) {
            val now = LocalDateTime.now()
            val payments = mutableListOf<Payment>()
            val events = mutableListOf<UketEvent>()
            val rounds = mutableListOf<UketEventRound>()
            val groups = mutableListOf<EntryGroup>()
            var roundStartId = 0

            println("event 및 round 생성")
            val doubleCount = count.toDouble()
            repeat(count) { i ->
                if (i % 100 == 0) {
                    println("${String.format("%.2f", i / doubleCount * 100)}%")
                }
                val ticketingStart = now.plusDays(Random.nextLong(-10, 10))
                val ticketingEnd = now.plusDays(Random.nextLong(0, 3))

                val roundDate = ticketingEnd.plusDays(Random.nextLong(1, 30))
                val roundSize = Random.nextInt(1, 3)
                val roundDateTimeList = mutableListOf<LocalDateTime>()
                repeat(roundSize) { s ->
                    roundDateTimeList.add(roundDate.plusDays(s.toLong()))
                }
                val uketEventRounds = createUketEventsRoundsWithDateAndId(roundDateTimeList, roundStartId.toLong())
                roundStartId += uketEventRounds.size

                val event = createUketEventWithNameAndId(
                    ticketingStart,
                    ticketingEnd,
                    "행사$i",
                    i.toLong() + 1,
                    uketEventRounds
                )

                val payment = createPaymentWithIdAndEventId(
                    event.id,
                    i.toLong() + 1
                )

                events.add(event)
                payments.add(payment)
                rounds.addAll(event.uketEventRounds)
            }

            println("round에 대한 entryGroup 생성")
            val size = rounds.size.toDouble()
            rounds.forEach {
                if (it.id.toInt() % 100 == 0) {
                    println("${String.format("%.2f", it.id / size * 100)}%")
                }
                val entryGroups =
                    createEntryGroupWithTime(it, listOf(it.eventRoundDateTime, it.eventRoundDateTime.plusHours(1)))
                groups.addAll(entryGroups)
            }

            val file = File("dummyDataSqlDump")

            println("-- Insert Events")
            events.forEach {
                file.appendText(toInsertSqlForUketEvent(it) + "\n")
            }

            println("-- Insert Payments")
            payments.forEach {
                file.appendText(toInsertSqlForPayment(it) + "\n")
            }

            println("-- Insert Event Rounds")
            rounds.forEach {
                file.appendText(toInsertSqlForUketEventRound(it) + "\n")
            }

            println("-- Insert Entry Groups")
            groups.forEach {
                file.appendText(toInsertSqlForEntryGroup(it) + "\n")
            }
        }

        private fun createUketEventWithNameAndId(
            ticketingStartDateTime: LocalDateTime,
            ticketingEndDateTime: LocalDateTime,
            eventName: String,
            id: Long,
            uketEventRounds: List<UketEventRound>,
        ): UketEvent {
            val easyRandom = EasyRandom(
                EasyRandomParameters()
                    .randomize(named("id")) { id }
                    .randomize(named("eventName")) { eventName }
                    .randomize(named("banners")) { listOf<Banner>() }
                    .randomize(named("uketEventRounds")) {
                        uketEventRounds.map {
                            UketEventRound(
                                id = it.id,
                                uketEvent = null,
                                eventRoundDateTime = it.eventRoundDateTime,
                                ticketingStartDateTime = ticketingStartDateTime,
                                ticketingEndDateTime = ticketingEndDateTime
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

        private fun createPaymentWithIdAndEventId(id: Long, eventId: Long): Payment {
            val easyRandom = EasyRandom(
                EasyRandomParameters()
                    .randomize(named("id")) { id }
                    .randomize(named("uketEventId")) { eventId }
                    .randomize(named("ticketPrice")) { Random.nextInt(1000, 3000) }
            )
            val payment = easyRandom.nextObject(Payment::class.java)

            return payment
        }

        private fun createUketEventsRoundsWithDateAndId(
            uketEventRoundDates: List<LocalDateTime>,
            startId: Long = 0L,
        ): List<UketEventRound> {
            var id = startId
            val eventRounds = uketEventRoundDates.map {
                id += 1
                val easyRandomEventRound = EasyRandom(
                    EasyRandomParameters()
                        .randomize(named("id")) { id }
                        .randomize(named("uketEvent")) { null }
                        .randomize(named("eventRoundDateTime")) { it }
                )
                easyRandomEventRound.nextObject(UketEventRound::class.java)
            }

            return eventRounds
        }

        private fun toInsertSqlForEntryGroup(entryGroup: EntryGroup): String {
            val tableName = "entry_group"

            val columns = mutableListOf<String>()
            val values = mutableListOf<String>()

            // 직접 필드 접근 (중첩 필드도 분해해서 처리)
            with(entryGroup) {
                // 일반 필드
                columns += listOf(
                    "uket_event_round_id",
                    "entry_group_name",
                    "entry_start_datetime",
                    "entry_end_datetime",
                    "ticket_count",
                    "total_ticket_count",
                    "created_at",
                    "updated_at"
                )

                values += listOf(
                    uketEventRound.id,
                    entryGroupName,
                    entryStartDateTime,
                    entryEndDateTime,
                    ticketCount,
                    totalTicketCount,
                    createdAt,
                    updatedAt
                ).map { toSqlValue(it) }
            }

            return "INSERT INTO $tableName (${columns.joinToString(", ")}) VALUES (${values.joinToString(", ")});"
        }

        private fun toInsertSqlForUketEventRound(uketEventRound: UketEventRound): String {
            val tableName = "uket_event_round"

            val columns = mutableListOf<String>()
            val values = mutableListOf<String>()

            // 직접 필드 접근 (중첩 필드도 분해해서 처리)
            with(uketEventRound) {
                // 일반 필드
                columns += listOf(
                    "uket_event_id",
                    "event_round_datetime",
                    "ticketing_start_datetime",
                    "ticketing_end_datetime",
                    "created_at",
                    "updated_at"
                )

                values += listOf(
                    uketEvent!!.id,
                    eventRoundDateTime,
                    ticketingStartDateTime,
                    ticketingEndDateTime,
                    createdAt,
                    updatedAt
                ).map { toSqlValue(it) }
            }

            return "INSERT INTO $tableName (${columns.joinToString(", ")}) VALUES (${values.joinToString(", ")});"
        }

        private fun toInsertSqlForUketEvent(event: UketEvent): String {
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

        private fun toInsertSqlForPayment(payment: Payment): String {
            val tableName = "payment"

            val columns = mutableListOf<String>()
            val values = mutableListOf<String>()

            // 직접 필드 접근 (중첩 필드도 분해해서 처리)
            with(payment) {
                // 일반 필드
                columns += listOf(
                    "uket_event_id",
                    "ticket_price",
                    "bank_code",
                    "account_number",
                    "depositor_name",
                    "deposit_url",
                    "created_at",
                    "updated_at"
                )

                values += listOf(
                    uketEventId,
                    ticketPrice,
                    bankCode,
                    accountNumber,
                    depositorName,
                    depositUrl,
                    createdAt,
                    updatedAt
                ).map { toSqlValue(it) }
            }

            return "INSERT INTO $tableName (${columns.joinToString(", ")}) VALUES (${values.joinToString(", ")});"
        }

        private fun toSqlValue(value: Any?): String = when (value) {
            null -> "NULL"
            is String -> "'${value.replace("'", "''")}'"
            is Enum<*> -> "'${value.name}'"
            is LocalDateTime -> "'$value'"
            is Boolean -> if (value) "1" else "0"
            else -> value.toString()
        }
    }
}
