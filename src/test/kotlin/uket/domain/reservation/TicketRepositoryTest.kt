package uket.domain.reservation

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import jakarta.persistence.EntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional
import uket.common.enums.EventType
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.reservation.repository.TicketRepository
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import java.time.LocalDateTime

@DataJpaTest
@Transactional
class TicketRepositoryTest(
    private val ticketRepository: TicketRepository,
    private val entityManager: EntityManager,
) : DescribeSpec({

        lateinit var savedUketEvent: UketEvent
        lateinit var savedUketEventRound: UketEventRound
        lateinit var savedEntryGroup: EntryGroup
        lateinit var savedTicket: Ticket

        beforeEach {
            val uketEventRound = UketEventRound(
                uketEvent = null,
                eventRoundDateTime = LocalDateTime.now()
            )

            entityManager.persist(uketEventRound)
            val uketEvent = UketEvent(
                organizationId = 1L,
                eventName = "uketEventA",
                eventType = EventType.FESTIVAL,
                location = "00시00구",
                ticketingStartDateTime = LocalDateTime.now(),
                ticketingEndDateTime = LocalDateTime.now(),
                ticketPrice = 0,
                totalTicketCount = 0,
                details = UketEvent.EventDetails(
                    "", "", UketEvent.EventContact(UketEvent.EventContact.ContactType.INSTAGRAM, "")
                ),
                eventImageId = "",
                thumbnailImageId = "",
            )
            uketEvent.addUketEventRound(uketEventRound)
            entityManager.persist(uketEvent)

            // 엔트리 그룹 저장
            val entryGroup = EntryGroup(
                uketEventRound = uketEventRound,
                entryGroupName = "nameA",
                entryStartDateTime = LocalDateTime.now(),
                entryEndDateTime = LocalDateTime.now(),
                ticketCount = 0,
                totalTicketCount = 10
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

            savedUketEvent = uketEvent
            savedUketEventRound = uketEventRound
            savedEntryGroup = entryGroup
            savedTicket = ticket

            entityManager.clear()
        }

        describe("findAllByUserIdAndStatusNotWithEntryGroup") {
            it("정상적으로 티켓을 조회한다") {
                val tickets = ticketRepository.findAllByUserIdAndStatusNotWithEntryGroup(
                    userId = savedTicket.userId,
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

        describe("findByUserIdAndId") {
            it("정상적인 티켓 조회 시") {
                val ticket = ticketRepository.findByUserIdAndId(
                    userId = savedTicket.userId,
                    ticketId = savedTicket.id,
                )
                ticket shouldNotBe null
                ticket!!.ticketNo shouldBe ticket.ticketNo
            }

            it("조건에 맞는 티켓이 없을 때") {
                val ticket = ticketRepository.findByUserIdAndId(
                    userId = 999L,
                    ticketId = savedTicket.id,
                )
                ticket shouldBe null
            }
        }
    }) {
    override fun extensions(): List<Extension> {
        return super.extensions() + listOf(SpringExtension) // SpringExtension 활성화
    }
}
