package uket.domain.uketEvent

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import uket.domain.uketEvent.util.UketEventRandomUtil
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.repository.UketEventRoundRepository
import uket.domain.uketevent.service.UketEventRoundService
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class UketEventRoundServiceTest :
    DescribeSpec({

        isolationMode = IsolationMode.InstancePerLeaf

        val uketEventRoundRepository: UketEventRoundRepository = mockk<UketEventRoundRepository>()
        val uketEventRoundService: UketEventRoundService = UketEventRoundService(uketEventRoundRepository)

        describe("getNowTicketingRounds") {
            context("티켓팅이 진행되고 있는 회차가 2개일 때") {
                it("티켓팅이 진행되고 있는 회차 조회") {
                    val (eventId, uketEventRounds) = setDB()
                    every {
                        uketEventRoundRepository.findByUketEventIdAndEventRoundDateAfter(
                            eventId,
                            LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)
                        )
                    } returns uketEventRounds

                    val findRounds = uketEventRoundService.getNowTicketingRounds(eventId, LocalDateTime.now())
                    findRounds.size shouldBe 2
                }
            }
        }

        describe("getEventRoundsMapByEventIds") {
            context("이벤트 2개에 대한 회차 목록 각각 2개씩 존재할 때") {
                it("이벤트 회차 맵 조회") {
                    val (uketEvents, uketEventRounds) = setDB2()
                    val eventIds = uketEvents.map { it.id }
                    every {
                        uketEventRoundRepository.findAllByUketEventIdInWithUketEvent(eventIds)
                    } returns uketEventRounds
                    val map = uketEventRoundService.getEventRoundsMapByEventIds(eventIds)
                    map.keys.size shouldBe 2
                    map.get(uketEvents[0].id)!!.size shouldBe 2
                    map.get(uketEvents[1].id)!!.size shouldBe 2
                }
            }
        }
    }) {
    companion object {
        private fun setDB(): Pair<Long, List<UketEventRound>> {
            val now = LocalDateTime.now()

            val uketEvent =
                UketEventRandomUtil.createUketEvent()
            val dates = listOf(10L, 8L)
            val uketEventRounds = List(2) {
                UketEventRandomUtil.createUketEventRound(
                    uketEvent,
                    now.plusDays(dates[it]),
                    now.minusDays(3),
                    now.plusDays(2)
                )
            }

            return Pair(uketEvent.id, listOf(uketEventRounds[0], uketEventRounds[1]))
        }

        fun setDB2(): Pair<List<UketEvent>, List<UketEventRound>> {
            val now = LocalDateTime.now()

            val uketEvent =
                UketEventRandomUtil.createUketEvent(
                    listOf(),
                    "eventA",
                    1L,
                )
            val dates = listOf(10L, 8L)
            val uketEventRounds = List(2) {
                UketEventRandomUtil.createUketEventRound(
                    uketEvent,
                    now.plusDays(dates[it]),
                    now.minusDays(3),
                    now.plusDays(2)
                )
            }

            val uketEvent2 =
                UketEventRandomUtil.createUketEvent(
                    listOf(),
                    "eventB",
                    2L
                )
            val uketEventRounds2 = List(2) {
                UketEventRandomUtil.createUketEventRound(
                    uketEvent2,
                    now.plusDays(dates[it]),
                    now.minusDays(3),
                    now.plusDays(2)
                )
            }

            val rounds = mutableListOf<UketEventRound>()
            rounds.addAll(uketEventRounds)
            rounds.addAll(uketEventRounds2)

            return Pair(listOf(uketEvent, uketEvent2), rounds)
        }
    }
}
