package uket.domain.uketEvent

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import jakarta.persistence.EntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import uket.common.enums.EventType
import uket.domain.admin.entity.Organization
import uket.domain.uketEvent.util.UketEventRandomUtil
import uket.domain.uketevent.entity.Banner
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

        describe("findAllByEventEndDateBeforeNowWithUketEventRound") {
            context("티켓팅 중인 이벤트 3개가 있는 경우") {
                it("3개의 이벤트 조회") {
                    setDB2(entityManager)

                    val eventList = uketEventRepository.findAllByEventEndDateAfterNowWithUketEventRound()
                    eventList.size shouldBe 3
                }
            }

            context("티켓팅 중인 이벤트 2개, 행사가 종료된 이벤트 1개가 있는 경우") {
                it("조건에 맞는 단체가 없는 경우") {
                    setDB3(entityManager)

                    val eventList = uketEventRepository.findAllByEventEndDateAfterNowWithUketEventRound()
                    eventList.size shouldBe 2
                }
            }
        }

        describe("findByIdWithBanners") {
            context("이벤트가 1개 있고, 배너가 없는 경우") {
                it("해당 이벤트를 찾는 경우") {
                    val event = setDB4(entityManager)

                    println("event.id = ${event.id}")
                    val findEvent = uketEventRepository.findByIdAndLastRoundDateAfterNowWithBanners(event.id)
                    findEvent shouldNotBe null
                    findEvent!!.banners.size shouldBe 0
                }
                it("해당 이벤트가 아닌 이벤트를 찾는 경우") {
                    val event = setDB4(entityManager)

                    val findEvent = uketEventRepository.findByIdAndLastRoundDateAfterNowWithBanners(999L)
                    findEvent shouldBe null
                }
            }

            context("이벤트가 1개 있고, 배너가 1개 있는 경우") {
                it("해당 이벤트를 찾는 경우") {
                    val event = setDB5(entityManager)

                    println("event.id = ${event.id}")
                    val findEvent = uketEventRepository.findByIdAndLastRoundDateAfterNowWithBanners(event.id)
                    findEvent shouldNotBe null
                    findEvent!!.banners.size shouldBe 3
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

            val uketEventRounds = UketEventRandomUtil.createUketEventRounds(
                listOf(now.plusDays(3)),
                now.minusDays(2),
                now.plusDays(2)
            )
            val banners = listOf(
                Banner(id = 0L, uketEvent = null, imageId = 1, link = "link1"),
                Banner(id = 0L, uketEvent = null, imageId = 2, link = "link2"),
                Banner(id = 0L, uketEvent = null, imageId = 3, link = "link3")
            )
            val uketEvent =
                UketEventRandomUtil.createUketEvent(
                    uketEventRounds,
                    banners
                )

            entityManager.persist(uketEvent)
            entityManager.flush()
            return uketEvent
        }

        private fun setDB4(entityManager: EntityManager): UketEvent {
            val now = LocalDateTime.now()

            val uketEventRounds = UketEventRandomUtil.createUketEventRounds(
                listOf(now.plusDays(3)),
                now.minusDays(2),
                now.plusDays(2)
            )
            val uketEvent =
                UketEventRandomUtil.createUketEvent(
                    uketEventRounds
                )

            entityManager.persist(uketEvent)
            entityManager.flush()
            return uketEvent
        }

        private fun setDB3(entityManager: EntityManager) {
            val now = LocalDateTime.now()

            val uketEventRounds1 =
                UketEventRandomUtil.createUketEventRounds(
                    listOf(now.plusDays(6), now.plusDays(7)),
                    now.minusDays(2),
                    now.plusDays(2)
                )
            val uketEvent1 =
                UketEventRandomUtil.createUketEvent(
                    uketEventRounds1
                )
            entityManager.persist(uketEvent1)

            val uketEventRounds2 =
                UketEventRandomUtil.createUketEventRounds(
                    listOf(now.plusDays(8)),
                    now.minusDays(3),
                    now.plusDays(3)
                )
            val uketEvent2 =
                UketEventRandomUtil.createUketEvent(
                    uketEventRounds2
                )
            entityManager.persist(uketEvent2)

            val uketEventRounds3 = UketEventRandomUtil.createUketEventRounds(
                listOf(now.minusDays(2), now.minusDays(1)),
                now.minusDays(4),
                now.minusDays(3)
            )
            val closedEvent = UketEventRandomUtil.createUketEvent(
                uketEventRounds3
            )
            entityManager.persist(closedEvent)

            entityManager.flush()
        }

        private fun setDB2(entityManager: EntityManager) {
            val now = LocalDateTime.now()

            val uketEventRounds1 =
                UketEventRandomUtil.createUketEventRounds(
                    listOf(now.plusDays(3), now.plusDays(4)),
                    now.minusDays(2),
                    now.plusDays(2)
                )
            val uketEvent1 =
                UketEventRandomUtil.createUketEvent(
                    uketEventRounds1
                )
            entityManager.persist(uketEvent1)

            val uketEventRounds2 =
                UketEventRandomUtil.createUketEventRounds(
                    listOf(now.plusDays(7), now.plusDays(8)),
                    now.minusDays(3),
                    now.plusDays(3)
                )
            val uketEvent2 =
                UketEventRandomUtil.createUketEvent(
                    uketEventRounds2
                )
            entityManager.persist(uketEvent2)

            val uketEventRounds3 =
                UketEventRandomUtil.createUketEventRounds(
                    listOf(now.plusDays(5)),
                    now.minusDays(4),
                    now.plusDays(4)
                )
            val uketEvent3 =
                UketEventRandomUtil.createUketEvent(
                    uketEventRounds3
                )
            entityManager.persist(uketEvent3)

            entityManager.flush()
        }

        private fun setDB(entityManager: EntityManager): Long {
            val organization = Organization(
                name = "organizationA",
                organizationImageId = null,
            )
            entityManager.persist(organization)

            val uketEventRound = UketEventRound(
                uketEvent = null,
                eventRoundDateTime = LocalDateTime.now(),
                ticketingStartDateTime = LocalDateTime.now(),
                ticketingEndDateTime = LocalDateTime.now()
            )

            val uketEvent = UketEvent(
                organizationId = organization.id,
                eventName = "uketEventA",
                eventType = EventType.FESTIVAL,
                location = "locationA",
                ticketPrice = 1000,
                totalTicketCount = 0,
                buyTicketLimit = 4,
                details = UketEvent.EventDetails(
                    "", "", UketEvent.EventContact(UketEvent.EventContact.ContactType.INSTAGRAM, "", "")
                ),
                eventImageId = "",
                thumbnailImageId = "",
                _uketEventRounds = listOf(uketEventRound),
                _banners = listOf(),
            )

            entityManager.persist(uketEvent)
            entityManager.flush()

            return uketEvent.id
        }
    }
}
