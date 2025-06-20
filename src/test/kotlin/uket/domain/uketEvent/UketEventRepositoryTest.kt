package uket.domain.uketEvent

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import uket.common.enums.EventContactType
import uket.common.enums.EventType
import uket.domain.admin.entity.Organization
import uket.domain.uketEvent.util.UketEventRandomUtil
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.uketevent.repository.UketEventRepository
import java.time.LocalDateTime

@DataJpaTest
class UketEventRepositoryTest(
    private val uketEventRepository: UketEventRepository,
    private val entityManager: EntityManager,
) : DescribeSpec({

        isolationMode = IsolationMode.InstancePerTest

        describe("findOrganizationNameByUketEventId") {
            context("단체와 이벤트가 연결되어 있을 때") {
                it("정상적으로 단체 이름 조회") {
                    val uketEventId = setDB(entityManager)
                    val name = uketEventRepository.findOrganizationNameByUketEventId(uketEventId = uketEventId)
                    name shouldBe "organizationA"
                }

                it("조건에 맞는 단체가 없는 경우") {
                    setDB(entityManager)
                    val name = uketEventRepository.findOrganizationNameByUketEventId(uketEventId = 999L)
                    name shouldBe null
                }
            }
        }
    }
    ) {
    override fun extensions(): List<Extension> {
        return super.extensions() + listOf(SpringExtension) // SpringExtension 활성화
    }

    companion object {
        private fun setDB5(entityManager: EntityManager): UketEvent {
            val now = LocalDateTime.now()

            val uketEvent = UketEventRandomUtil.createUketEvent()

            val uketEventRound = UketEventRandomUtil.createUketEventRound(
                uketEvent,
                now.plusDays(3),
                now.minusDays(2),
                now.plusDays(2)
            )

            entityManager.persist(uketEvent)
            entityManager.persist(uketEventRound)
            entityManager.flush()
            return uketEvent
        }

        private fun setDB4(entityManager: EntityManager): UketEvent {
            val now = LocalDateTime.now()

            val uketEvent =
                UketEventRandomUtil.createUketEvent()

            val uketEventRound = UketEventRandomUtil.createUketEventRound(
                uketEvent,
                now.plusDays(3),
                now.minusDays(2),
                now.plusDays(2)
            )

            entityManager.persist(uketEvent)
            entityManager.persist(uketEventRound)
            entityManager.flush()
            return uketEvent
        }

        private fun setDB3(entityManager: EntityManager) {
            val now = LocalDateTime.now()

            val uketEvent1 =
                UketEventRandomUtil.createUketEvent()
            val uketEventRounds1 = List(2) { index ->
                UketEventRandomUtil.createUketEventRound(
                    uketEvent1,
                    now.plusDays((6 + index).toLong()),
                    now.minusDays(2),
                    now.plusDays(2)
                )
            }
            entityManager.persist(uketEvent1)
            uketEventRounds1.forEach { entityManager.persist(it) }

            val uketEvent2 =
                UketEventRandomUtil.createUketEvent()
            val uketEventRound2 =
                UketEventRandomUtil.createUketEventRound(
                    uketEvent2,
                    now.plusDays(8),
                    now.minusDays(3),
                    now.plusDays(3)
                )
            entityManager.persist(uketEvent2)
            entityManager.persist(uketEventRound2)

            val closedEvent = UketEventRandomUtil.createUketEvent()
            val uketEventRounds3 = List(2) { index ->
                UketEventRandomUtil.createUketEventRound(
                    closedEvent,
                    now.minusDays((1 + index).toLong()),
                    now.minusDays(4),
                    now.minusDays(3)
                )
            }
            entityManager.persist(closedEvent)
            uketEventRounds3.forEach { entityManager.persist(it) }

            entityManager.flush()
        }

        private fun setDB2(entityManager: EntityManager) {
            val now = LocalDateTime.now()

            val uketEvent1 =
                UketEventRandomUtil.createUketEvent()
            val uketEventRounds1 = List(2) { index ->
                UketEventRandomUtil.createUketEventRound(
                    uketEvent1,
                    now.plusDays((3 + index).toLong()),
                    now.minusDays(2),
                    now.plusDays(2)
                )
            }
            entityManager.persist(uketEvent1)
            uketEventRounds1.forEach { entityManager.persist(it) }

            val uketEvent2 =
                UketEventRandomUtil.createUketEvent()
            val uketEventRounds2 = List(2) { index ->
                UketEventRandomUtil.createUketEventRound(
                    uketEvent2,
                    now.plusDays((7 + index).toLong()),
                    now.minusDays(3),
                    now.plusDays(3)
                )
            }
            entityManager.persist(uketEvent2)
            uketEventRounds2.forEach { entityManager.persist(it) }

            val uketEvent3 =
                UketEventRandomUtil.createUketEvent()
            val uketEventRounds3 =
                UketEventRandomUtil.createUketEventRound(
                    uketEvent3,
                    now.plusDays(5),
                    now.minusDays(4),
                    now.plusDays(4)
                )
            entityManager.persist(uketEvent3)
            entityManager.persist(uketEventRounds3)

            entityManager.flush()
        }

        private fun setDB(entityManager: EntityManager): Long {
            val organization = Organization(
                name = "organizationA",
                organizationImageId = null,
            )
            entityManager.persist(organization)

            val uketEvent = UketEvent(
                organizationId = organization.id,
                eventName = "uketEventA",
                eventType = EventType.FESTIVAL,
                location = "locationA",
                ticketPrice = 1000,
                totalTicketCount = 0,
                buyTicketLimit = 4,
                details = UketEvent.EventDetails(
                    "", "", UketEvent.EventContact(EventContactType.INSTAGRAM, "", "")
                ),
                eventImageId = "",
                thumbnailImageId = "",
                firstRoundDateTime = LocalDateTime.now(),
                lastRoundDateTime = LocalDateTime.now()
            )

            val uketEventRound = UketEventRound(
                uketEventId = uketEvent.id,
                eventRoundDateTime = LocalDateTime.now(),
                ticketingStartDateTime = LocalDateTime.now(),
                ticketingEndDateTime = LocalDateTime.now()
            )

            entityManager.persist(uketEvent)
            entityManager.flush()

            return uketEvent.id
        }
    }
}
