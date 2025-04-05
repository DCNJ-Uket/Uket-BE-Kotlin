package uket.domain.reservation

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.repository.TicketRepository
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.enums.EventType
import java.time.LocalDateTime

@DataJpaTest
@Transactional
class TicketRepositoryTest(
    private val ticketRepository: TicketRepository,
    private val entityManager: EntityManager,
) : DescribeSpec({

        beforeEach {
            val uketEvent = UketEvent(
                organizationId = 1L,
                name = "uketEventA",
                eventType = EventType.FESTIVAL,
                location = "locationA",
                eventImagePath = null,
                displayEndDate = LocalDateTime.now(),
                ticketPrice = 1000,
            )
            entityManager.persist(uketEvent)

            val uketEventRound = UketEventRound(
                uketEvent = uketEvent,
                name = "uketEventRoundA",
                eventDate = LocalDateTime.now(),
                ticketingDateTime = LocalDateTime.now(),
            )
            entityManager.persist(uketEventRound)

            // 엔트리 그룹 저장
            val entryGroup = EntryGroup(
                uketEventRound = uketEventRound,
                name = "entryGroupA",
                entryStartTime = LocalDateTime.now(),
                entryEndTime = LocalDateTime.now(),
                reservationCount = 0,
                totalCount = 10,
            )
            entityManager.persist(entryGroup)

            // 티켓 저장
            val ticket = Ticket(
                userId = 1L,
                entryGroupId = entryGroup.id,
                status = TicketStatus.BEFORE_ENTER,
                ticketNo = "ticketA",
                enterAt = null,
            )
            entityManager.persist(ticket)

            entityManager.flush()
            entityManager.clear()
        }

        describe("findAllByUserIdAndStatusNotWithEntryGroup") {
            it("정상적으로 티켓을 조회한다") {
                val tickets = ticketRepository.findAllByUserIdAndStatusNotWithEntryGroup(
                    userId = 1L,
                    status = TicketStatus.RESERVATION_CANCEL,
                )
                tickets.size shouldBe 1
                tickets[0].ticketNo shouldBe "ticketA"
            }

            it("조건에 맞는 티켓이 없으면 빈 리스트를 반환한다") {
                val tickets = ticketRepository.findAllByUserIdAndStatusNotWithEntryGroup(
                    userId = 999L,
                    status = TicketStatus.RESERVATION_CANCEL,
                )
                tickets shouldBe emptyList()
            }
        }
    }) {
    override fun extensions(): List<Extension> {
        return super.extensions() + listOf(SpringExtension) // SpringExtension 활성화
    }
}
