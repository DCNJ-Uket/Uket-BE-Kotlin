package uket.domain.uketEvent

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional
import uket.common.enums.EventType
import uket.domain.admin.entity.Organization
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.repository.UketEventRepository
import java.time.LocalDateTime

@DataJpaTest
@Transactional
class UketEventRepositoryTest(
    private val uketEventRepository: UketEventRepository,
    private val entityManager: EntityManager,
) : DescribeSpec({

        isolationMode = IsolationMode.InstancePerTest

        lateinit var savedOrganization: Organization
        lateinit var savedUketEvent: UketEvent

        beforeEach {
            val organization = Organization(
                name = "organizationA",
                organizationImagePath = null,
            )
            entityManager.persist(organization)
            entityManager.flush()
            savedOrganization = organization

            val uketEvent = UketEvent(
                organizationId = organization.id,
                name = "uketEventA",
                eventType = EventType.FESTIVAL,
                location = "locationA",
                eventImagePath = null,
                displayEndDate = LocalDateTime.now(),
                ticketPrice = 1000,
            )
            entityManager.persist(uketEvent)
            entityManager.flush()
            savedUketEvent = uketEvent

            entityManager.clear()
        }

        describe("findOrganizationNameByUketEventId") {
            it("정상적으로 단체 이름 조회") {
                val name = uketEventRepository.findOrganizationNameByUketEventId(uketEventId = savedUketEvent.id)
                name shouldBe "organizationA"
            }

            it("조건에 맞는 단체가 없는 경우") {
                val name = uketEventRepository.findOrganizationNameByUketEventId(uketEventId = 999L)
                name shouldBe null
            }
        }
    }) {
    override fun extensions(): List<Extension> {
        return super.extensions() + listOf(SpringExtension) // SpringExtension 활성화
    }
}
