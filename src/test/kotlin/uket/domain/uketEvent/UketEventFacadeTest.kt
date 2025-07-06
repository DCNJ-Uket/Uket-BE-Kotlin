package uket.domain.uketEvent

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import uket.domain.uketEvent.util.SqlGenerator
import uket.domain.uketEvent.util.UketEventRandomUtil
import uket.domain.uketEvent.util.UketEventRandomUtil.Companion.createEntryGroup
import uket.domain.uketEvent.util.UketEventRandomUtil.Companion.createUketEvent
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import java.io.File
import java.time.LocalDateTime
import kotlin.random.Random

class UketEventFacadeTest :
    DescribeSpec({

        isolationMode = IsolationMode.InstancePerLeaf

//        it("더미데이터 출력") {
//            createDummyData(100)
//        }
    }) {
    companion object {
        fun createDummyData(count: Int) {
            val now = LocalDateTime.now()

            println("events 생성")
            val events = mutableListOf<UketEvent>()
            repeat(count) { i ->
                val eventId = i + 1L

                val event = createUketEvent(
                    "행사$eventId",
                    eventId,
                )

                events.add(event)
            }

            println("rounds 생성")
            val rounds = mutableListOf<UketEventRound>()
            var roundId = 0
            events.forEach { event ->
                val roundSize = Random.nextInt(1, 3)

                val ticketingStart = now.plusDays(Random.nextLong(-10, 10))
                val ticketingEnd = now.plusDays(Random.nextLong(0, 3))

                val roundDate = ticketingEnd.plusDays(Random.nextLong(1, 30))
                val roundDateTimes = mutableListOf<LocalDateTime>()
                repeat(roundSize) { s ->
                    roundDateTimes.add(roundDate.plusDays(s.toLong()))
                }

                val uketEventRounds = List(roundSize) { index ->
                    roundId += 1
                    UketEventRandomUtil.createUketEventRound(
                        event,
                        roundDateTimes[index],
                        ticketingStart,
                        ticketingEnd,
                        roundId.toLong()
                    )
                }
                rounds.addAll(uketEventRounds)
            }

            println("entryGroup 생성")
            val groups = mutableListOf<EntryGroup>()
            rounds.forEach { round ->
                val entryGroups = List(2) { index ->
                    createEntryGroup(
                        round,
                        round.eventRoundDateTime.plusHours(index.toLong()),
                        eventId = round.uketEventId
                    )
                }
                groups.addAll(entryGroups)
            }

            val file = File("dummyDataSqlDump")

            println("-- Insert Events")
            events.forEach {
                file.appendText(SqlGenerator.toInsertSqlForUketEvent(it) + "\n")
            }

            println("-- Insert Event Rounds")
            rounds.forEach {
                file.appendText(SqlGenerator.toInsertSqlForUketEventRound(it) + "\n")
            }

            println("-- Insert Entry Groups")
            groups.forEach {
                file.appendText(SqlGenerator.toInsertSqlForEntryGroup(it) + "\n")
            }
        }
    }
}
