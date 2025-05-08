package uket.domain.uketEvent

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
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
    }) {
    companion object {
        private fun setDB(): Pair<Long, List<UketEventRound>> {
            val now = LocalDateTime.now()

            val uketEventRounds =
                UketEventRandomUtil.createUketEventsRoundsWithDate(
                    listOf(
                        now.plusDays(10),
                        now.plusDays(8)
                    ),
                    now.minusDays(3),
                    now.plusDays(2)
                )

            val uketEvent =
                UketEventRandomUtil.createUketEvent(
                    uketEventRounds,
                    listOf()
                )

            return Pair(uketEvent.id, listOf(uketEventRounds[0], uketEventRounds[1]))
        }
    }
}
