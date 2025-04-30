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

class UketEventServiceTest :
    DescribeSpec({

        isolationMode = IsolationMode.InstancePerLeaf

        val uketEventRepository: UketEventRepository = mockk<UketEventRepository>()
        val uketEventService: UketEventService = UketEventService(uketEventRepository)

        describe("유효한 행사 목록 조회") {
            context("티켓팅 진행 중인 행사 3개가 있을 때") {
                val (uketEvent1, uketEvent2, uketEvent3) = setDB(uketEventRepository)
                every { uketEventRepository.findAllByEventEndDateAfterNowWithUketEventRound() } returns listOf(
                    uketEvent1,
                    uketEvent2,
                    uketEvent3
                )

                it("행사 시작 순서대로 출력") {
                    val eventList = uketEventService.getActiveEventItemList("ALL")
                    eventList.size shouldBe 3
                    eventList.get(0).eventName shouldBe uketEvent3.eventName
                    eventList.get(1).eventName shouldBe uketEvent1.eventName
                    eventList.get(2).eventName shouldBe uketEvent2.eventName
                }
            }
            context("티켓팅 진행 중인 행사 2개, 티켓팅 시작 전인 행사 1개, 티켓팅 종료된 행사 1개가 있을 때") {
                val now = LocalDateTime.now()
                val (uketEvent1, uketEvent2) = setDB2(now)
                val (notOpenedEvent, closedEvent) = setDB3(now, uketEvent2)

                every { uketEventRepository.findAllByEventEndDateAfterNowWithUketEventRound() } returns listOf(
                    uketEvent1,
                    uketEvent2,
                    notOpenedEvent,
                    closedEvent
                )

                it("티켓팅 진행 중, 시작 전, 종료 순으로 출력") {
                    val eventList = uketEventService.getActiveEventItemList("ALL")
                    eventList.size shouldBe 4
                    eventList.get(0).eventName shouldBe uketEvent2.eventName
                    eventList.get(1).eventName shouldBe uketEvent1.eventName
                    eventList.get(2).eventName shouldBe notOpenedEvent.eventName
                    eventList.get(3).eventName shouldBe closedEvent.eventName
                }
            }
        }

        it("더미데이터 출력") {
            UketEventRandomUtil.createDummyData()
        }
    })

private fun setDB3(
    now: LocalDateTime,
    uketEvent2: UketEvent,
): Pair<UketEvent, UketEvent> {
    val uketEventRounds3 =
        UketEventRandomUtil.createUketEventsRoundWithDate(listOf(now.plusDays(7), now.plusDays(8)))
    val notOpenedEvent = UketEventRandomUtil.createUketEventWithDatesAndEventRoundsAndBanners(
        now.plusDays(3),
        now.plusDays(5),
        uketEventRounds3,
        listOf()
    )

    val uketEventRounds4 =
        UketEventRandomUtil.createUketEventsRoundWithDate(listOf(now.minusDays(2), now.plusDays(1)))
    val closedEvent = UketEventRandomUtil.createUketEventWithDatesAndEventRoundsAndBanners(
        now.minusDays(4),
        now.minusDays(3),
        uketEventRounds4,
        listOf()
    )
    return Pair(notOpenedEvent, closedEvent)
}

private fun setDB2(now: LocalDateTime): Pair<UketEvent, UketEvent> {
    val uketEventRounds1 = UketEventRandomUtil.createUketEventsRoundWithDate(listOf(now.plusDays(5), now.plusDays(6)))
    val uketEvent1 = UketEventRandomUtil.createUketEventWithDatesAndEventRoundsAndBanners(
        now.minusDays(2),
        now.plusDays(2),
        uketEventRounds1,
        listOf()
    )

    val uketEventRounds2 = UketEventRandomUtil.createUketEventsRoundWithDate(listOf(now.plusDays(7), now.plusDays(8)))
    val uketEvent2 = UketEventRandomUtil.createUketEventWithDatesAndEventRoundsAndBanners(
        now.minusDays(3),
        now.plusDays(3),
        uketEventRounds2,
        listOf()
    )
    return Pair(uketEvent1, uketEvent2)
}

private fun setDB(uketEventRepository: UketEventRepository): Triple<UketEvent, UketEvent, UketEvent> {
    val now = LocalDateTime.now()
    val uketEventRounds1 = UketEventRandomUtil.createUketEventsRoundWithDate(listOf(now.plusDays(7), now.plusDays(8)))
    val uketEvent1 = UketEventRandomUtil.createUketEventWithDatesAndEventRoundsAndBanners(
        now.minusDays(2),
        now.plusDays(2),
        uketEventRounds1,
        listOf()
    )

    val uketEventRounds2 = UketEventRandomUtil.createUketEventsRoundWithDate(listOf(now.plusDays(9), now.plusDays(10)))
    val uketEvent2 = UketEventRandomUtil.createUketEventWithDatesAndEventRoundsAndBanners(
        now.minusDays(3),
        now.plusDays(3),
        uketEventRounds2,
        listOf()
    )

    val uketEventRounds3 = UketEventRandomUtil.createUketEventsRoundWithDate(listOf(now.plusDays(5), now.plusDays(6)))
    val uketEvent3 = UketEventRandomUtil.createUketEventWithDatesAndEventRoundsAndBanners(
        now.minusDays(4),
        now.plusDays(4),
        uketEventRounds3,
        listOf()
    )
    return Triple(uketEvent1, uketEvent2, uketEvent3)
}
