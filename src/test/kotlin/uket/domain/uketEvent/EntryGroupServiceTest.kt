package uket.domain.uketEvent

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.repository.EntryGroupRepository
import uket.domain.uketevent.service.EntryGroupService
import java.time.LocalDateTime

class EntryGroupServiceTest :
    DescribeSpec({

        isolationMode = IsolationMode.InstancePerLeaf

        val entryGroupRepository: EntryGroupRepository = mockk<EntryGroupRepository>()
        val entryGroupService: EntryGroupService = EntryGroupService(entryGroupRepository)

        describe("입장 그룹 목록 조회") {
            context("입장 그룹 하나는 지금, 하나는 티켓 품절, 하나는 한 시간 뒤일 때") {
                val (uketEvent, uketEventRound, entryGroups) = setDB()
                every { entryGroupRepository.findByUketEventRoundId(uketEventRound.id, EntryGroup::class.java) } returns entryGroups

                it("입장 그룹 1개만 출력") {
                    val findEntryGroups = entryGroupService.findValidByUketEventRoundId(uketEventRound.id)
                    findEntryGroups.size shouldBe 1
                }
            }
        }
    }) {
    companion object {
        private fun setDB(): Triple<UketEvent, UketEventRound, List<EntryGroup>> {
            val now = LocalDateTime.now()

            val uketEventRounds =
                UketEventRandomUtil.createUketEventsRoundsWithDate(listOf(now.plusDays(7)))

            val uketEvent = UketEventRandomUtil.createUketEvent(
                now.minusDays(2),
                now.plusDays(2),
                uketEventRounds,
                listOf()
            )

            val entryGroups = UketEventRandomUtil.createEntryGroupWithTime(uketEventRounds[0], listOf(now.minusHours(1), now, now.plusHours(1)))
            entryGroups[1].totalTicketCount = 0
            entryGroups[1].ticketCount = 0

            return Triple(uketEvent, uketEventRounds[0], listOf(entryGroups[1], entryGroups[2]))
        }
    }
}
