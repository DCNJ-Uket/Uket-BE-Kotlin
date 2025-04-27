package uket.domain.uketEvent

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.date.after
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import uket.domain.uketevent.repository.UketEventRoundRepository
import java.time.LocalDate
import java.time.LocalDateTime

@DataJpaTest
class UketEventRoundRepositoryTest(
    private val uketEventRoundRepository: UketEventRoundRepository,
    private val entityManager: EntityManager,
) : DescribeSpec({

        isolationMode = IsolationMode.InstancePerTest

        describe("findByUketEventIdAAndEventRoundDateAfterNow") {
            context("회차가 2개 있고, 하나는 어제일 때") {
                it("회차 조회") {
                    val uketEventId = setDB(entityManager)
                    val uketEventRounds = uketEventRoundRepository.findByUketEventIdAAndEventRoundDateAfterNow(uketEventId = uketEventId)
                    uketEventRounds.size shouldBe 1
                    uketEventRounds[0].eventRoundDateTime.toLocalDate() shouldBe after(LocalDate.now().minusDays(1))
                }
            }
        }
    }
    ) {
    override fun extensions(): List<Extension> {
        return super.extensions() + listOf(SpringExtension) // SpringExtension 활성화
    }
}

private fun setDB(entityManager: EntityManager): Long {
    val now = LocalDateTime.now()
    val uketEvent =
        UketEventRandomUtil.createUketEventWithDates(
            now.minusDays(3),
            now.minusDays(2)
        )

    val uketEventRounds =
        UketEventRandomUtil.createUketEventsRoundWithDate(uketEvent, listOf(now.minusDays(1), now))
    entityManager.persist(uketEvent)
    entityManager.flush()

    return uketEvent.id
}
