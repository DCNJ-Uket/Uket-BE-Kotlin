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

        it("더미데이터 출력") {
            createDummyData(100)
        }
    }) {
    companion object {
        fun createDummyData(count: Int) {
            val now = LocalDateTime.now()
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
                val uketEventRounds = UketEventRandomUtil.createUketEventRounds(
                    roundDateTimeList,
                    ticketingStart,
                    ticketingEnd,
                    roundStartId.toLong(),
                    true
                )
                roundStartId += uketEventRounds.size

                val event = createUketEvent(
                    uketEventRounds,
                    listOf(),
                    "행사$i",
                    i.toLong() + 1,
                )

                events.add(event)
                rounds.addAll(event.uketEventRounds)
            }

            println("round에 대한 entryGroup 생성")
            val size = rounds.size.toDouble()
            rounds.forEach {
                if (it.id.toInt() % 100 == 0) {
                    println("${String.format("%.2f", it.id / size * 100)}%")
                }
                val entryGroups =
                    createEntryGroup(it, listOf(it.eventRoundDateTime, it.eventRoundDateTime.plusHours(1)))
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
