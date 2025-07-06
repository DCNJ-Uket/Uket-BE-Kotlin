package uket.domain.uketEvent

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import uket.domain.uketEvent.util.UketEventRandomUtil
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.repository.EntryGroupRepository
import uket.domain.uketevent.service.EntryGroupService
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class EntryGroupServiceTest :
    DescribeSpec({

        isolationMode = IsolationMode.InstancePerLeaf

        val entryGroupRepository: EntryGroupRepository = mockk<EntryGroupRepository>()
        val entryGroupService: EntryGroupService = EntryGroupService(entryGroupRepository)

        describe("findAllValidByRoundIdAndStarDateAfter") {
            context("입장 그룹 하나는 지금, 하나는 티켓 품절, 하나는 한 시간 뒤일 때") {
                val now = LocalDateTime.now()
                val (uketEvent, uketEventRound, entryGroups) = setDB(now)
                every {
                    entryGroupRepository.findByUketEventRoundIdAndStartDateAfter(
                        uketEventRound.id,
                        now.truncatedTo(ChronoUnit.DAYS)
                    )
                } returns entryGroups

                it("입장 그룹 1개만 출력") {
                    val findEntryGroups =
                        entryGroupService.findAllValidByRoundIdAndStarDateAfter(uketEventRound.id, now)
                    findEntryGroups.size shouldBe 1
                }
            }
        }
    }) {
    companion object {
        private fun setDB(now: LocalDateTime): Triple<UketEvent, UketEventRound, List<EntryGroup>> {
            val uketEvent = UketEventRandomUtil.createUketEvent()

            val uketEventRound =
                UketEventRandomUtil.createUketEventRound(
                    uketEvent,
                    now.plusDays(7),
                    now.minusDays(2),
                    now.plusDays(2),
                )

            val entryGroups = List(3) { index ->
                UketEventRandomUtil.createEntryGroup(
                    uketEventRound,
                    now.plusHours((index - 1).toLong()),
                    eventId = uketEvent.id
                )
            }

            entryGroups[1].totalTicketCount = 0
            entryGroups[1].ticketCount = 0

            return Triple(uketEvent, uketEventRound, listOf(entryGroups[1], entryGroups[2]))
        }
    }
}
