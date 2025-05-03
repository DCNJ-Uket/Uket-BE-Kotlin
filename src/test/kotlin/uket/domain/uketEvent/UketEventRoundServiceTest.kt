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

class UketEventRoundServiceTest :
    DescribeSpec({

        isolationMode = IsolationMode.InstancePerLeaf

        val uketEventRoundRepository: UketEventRoundRepository = mockk<UketEventRoundRepository>()
        val uketEventRoundService: UketEventRoundService = UketEventRoundService(uketEventRoundRepository)

        describe("findByUketEventIdAndDateAfter") {
            context("회차가 어제 오후, 오늘 오전, 내일 오후 3가지가 있을 때") {
                it("오늘 날짜 이후로 조회") {
                    val (eventId, uketEventRounds) = setDB()
                    every {
                        uketEventRoundRepository.findByUketEventIdAAndEventRoundDateAfter(
                            eventId,
                            LocalDateTime
                                .now()
                                .withHour(0)
                                .withMinute(0)
                                .withSecond(0)
                                .withNano(0)
                        )
                    } returns uketEventRounds

                    val findRounds = uketEventRoundService.findByUketEventIdAndDateAfter(eventId, LocalDateTime.now())
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
                        now.minusDays(1).withHour(20),
                        now.withHour(8),
                        now.plusDays(1).withHour(20)
                    )
                )

            val uketEvent =
                UketEventRandomUtil.createUketEvent(
                    now.minusDays(3),
                    now.minusDays(2),
                    uketEventRounds,
                    listOf()
                )

            return Pair(uketEvent.id, listOf(uketEventRounds[1], uketEventRounds[2]))
        }
    }
}
