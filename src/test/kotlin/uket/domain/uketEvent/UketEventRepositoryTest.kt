package uket.domain.uketEvent

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import uket.common.enums.EventType
import uket.domain.admin.entity.Organization
import uket.domain.uketevent.entity.UketEvent
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

        describe("findAllByEventEndDateBeforeNowWithUketEventRound") {
            context("티켓팅 중인 이벤트 3개가 있는 경우") {
                it("3개의 이벤트 조회") {
                    setDB2(entityManager)

                    val eventList = uketEventRepository.findAllByEventEndDateBeforeNowWithUketEventRound()
                    eventList.size shouldBe 3
                }
            }

            context("티켓팅 중인 이벤트 2개, 행사가 종료된 이벤트 1개가 있는 경우") {
                it("조건에 맞는 단체가 없는 경우") {
                    setDB3(entityManager)

                    val eventList = uketEventRepository.findAllByEventEndDateBeforeNowWithUketEventRound()
                    eventList.size shouldBe 2
                }
            }
        }
    }
    ) {
    override fun extensions(): List<Extension> {
        return super.extensions() + listOf(SpringExtension) // SpringExtension 활성화
    }
}

private fun setDB3(entityManager: EntityManager) {
    val now = LocalDateTime.now()
    val uketEvent1 =
        UketEventRandomUtil.createUketEventWithDates(
            now.minusDays(2),
            now.plusDays(2)
        )
    entityManager.persist(uketEvent1)
    val uketEventRounds1 =
        UketEventRandomUtil.createUketEventsRoundWithDate(uketEvent1, listOf(now.plusDays(6), now.plusDays(7)))
    uketEventRounds1.forEach { entityManager.persist(it) }

    val uketEvent2 =
        UketEventRandomUtil.createUketEventWithDates(
            now.minusDays(3),
            now.plusDays(3)
        )
    entityManager.persist(uketEvent2)
    val uketEventRounds2 =
        UketEventRandomUtil.createUketEventsRoundWithDate(uketEvent2, listOf(now.plusDays(8)))
    uketEventRounds2.forEach { entityManager.persist(it) }

    val closedEvent = UketEventRandomUtil.createUketEventWithDates(
        now.minusDays(4),
        now.minusDays(3)
    )
    entityManager.persist(closedEvent)
    val uketEventRounds3 = UketEventRandomUtil.createUketEventsRoundWithDate(
        closedEvent,
        listOf(now.minusDays(2), now.minusDays(1))
    )
    uketEventRounds3.forEach { entityManager.persist(it) }

    entityManager.flush()
}

private fun setDB2(entityManager: EntityManager) {
    val now = LocalDateTime.now()
    val uketEvent1 =
        UketEventRandomUtil.createUketEventWithDates(
            now.minusDays(2),
            now.plusDays(2)
        )

    entityManager.persist(uketEvent1)
    val uketEventRounds1 =
        UketEventRandomUtil.createUketEventsRoundWithDate(uketEvent1, listOf(now.plusDays(3), now.plusDays(4)))
    uketEventRounds1.forEach { entityManager.persist(it) }

    val uketEvent2 =
        UketEventRandomUtil.createUketEventWithDates(
            now.minusDays(3),
            now.plusDays(3)
        )
    entityManager.persist(uketEvent2)
    val uketEventRounds2 =
        UketEventRandomUtil.createUketEventsRoundWithDate(uketEvent2, listOf(now.plusDays(7), now.plusDays(8)))
    uketEventRounds2.forEach { entityManager.persist(it) }

    val uketEvent3 =
        UketEventRandomUtil.createUketEventWithDates(
            now.minusDays(4),
            now.plusDays(4)
        )
    entityManager.persist(uketEvent3)
    val uketEventRounds3 =
        UketEventRandomUtil.createUketEventsRoundWithDate(uketEvent3, listOf(now.plusDays(5)))
    uketEventRounds3.forEach { entityManager.persist(it) }

    entityManager.flush()
}

private fun setDB(entityManager: EntityManager): Long {
    val organization = Organization(
        name = "organizationA",
        organizationImagePath = null,
    )
    entityManager.persist(organization)

    val uketEvent = UketEvent(
        organizationId = organization.id,
        eventName = "uketEventA",
        eventType = EventType.FESTIVAL,
        location = "locationA",
        ticketPrice = 1000,
        ticketingStartDateTime = LocalDateTime.now(),
        ticketingEndDateTime = LocalDateTime.now(),
        totalTicketCount = 0,
        details = UketEvent.EventDetails(
            "", "", UketEvent.EventContact(UketEvent.EventContact.ContactType.INSTAGRAM, "")
        ),
        uketEventImageId = "",
        thumbnailImageId = "",
        bannerImageIds = listOf(),
        _uketEventRounds = listOf()
    )
    entityManager.persist(uketEvent)
    entityManager.flush()

    return uketEvent.id
}
