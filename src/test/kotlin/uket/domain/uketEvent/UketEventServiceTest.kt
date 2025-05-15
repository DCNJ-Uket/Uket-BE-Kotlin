package uket.domain.uketEvent

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.repository.UketEventRepository
import uket.domain.uketevent.service.UketEventService
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class UketEventServiceTest :
    DescribeSpec({
        isolationMode = IsolationMode.InstancePerLeaf

        val uketEventRepository = mockk<UketEventRepository>()
        val uketEventService = UketEventService(uketEventRepository)

        describe("findAllByLastRoundDateAfterOrderByFirstRoundDateTime") {
            context("종료된 행사 1개, 진행 중인 행사 2개") {
                val now = LocalDateTime.now()
                val uketEvents = setDB(now)
                every {
                    uketEventRepository.findAllByLastRoundDateAfterOrderByFirstRoundDateTime(
                        now.truncatedTo(
                            ChronoUnit.DAYS
                        )
                    )
                } returns listOf(uketEvents[0], uketEvents[1])
                it("모든 활성 이벤트 조회") {
                    val findEvents = uketEventService.findAllNowActiveOrdered(now)
                    findEvents.size shouldBe 2
                    findEvents[0].firstRoundDateTime <= findEvents[1].firstRoundDateTime
                }
            }
        }
    }) {
    companion object {
        fun setDB(now: LocalDateTime): List<UketEvent> {
            val uketEventRounds1 = UketEventRandomUtil.createUketEventsRoundsWithDate(
                listOf(now, now.plusDays(1), now.plusDays(2), now.plusDays(3), now.plusDays(4)),
                now.minusDays(4), now.minusDays(3)
            )
            val uketEvent1 = UketEventRandomUtil.createUketEvent(uketEventRounds1, listOf())

            val uketEventRounds2 = UketEventRandomUtil.createUketEventsRoundsWithDate(
                listOf(now.plusDays(1), now.plusDays(2), now.plusDays(3), now.plusDays(4)),
                now.minusDays(4), now.minusDays(3)
            )
            val uketEvent2 = UketEventRandomUtil.createUketEvent(uketEventRounds2, listOf())

            val uketEventRounds3 = UketEventRandomUtil.createUketEventsRoundsWithDate(
                listOf(now.minusDays(1)),
                now.minusDays(4), now.minusDays(3)
            )
            val uketEvent3 = UketEventRandomUtil.createUketEvent(uketEventRounds3, listOf())

            return listOf(uketEvent1, uketEvent2, uketEvent3)
        }
    }
}
