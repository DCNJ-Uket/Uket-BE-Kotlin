package uket.domain.uketEvent

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.date.after
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import uket.domain.uketEvent.util.UketEventRandomUtil
import uket.domain.uketevent.repository.UketEventRoundRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@DataJpaTest
class UketEventRoundRepositoryTest(
    private val uketEventRoundRepository: UketEventRoundRepository,
    private val entityManager: EntityManager,
) : DescribeSpec({

        isolationMode = IsolationMode.InstancePerTest

        describe("findByUketEventIdAAndEventRoundDateAfterNow") {
            context("회차가 2개 있고, 하나는 어제일 때") {
                it("1개의 회차 조회") {
                    val now = LocalDateTime.now()
                    val uketEventId = setDB(entityManager, now)
                    val uketEventRounds = uketEventRoundRepository.findByUketEventIdAndEventRoundDateAfter(
                        uketEventId,
                        now.truncatedTo(
                            ChronoUnit.DAYS
                        )
                    )
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

    companion object {
        private fun setDB(entityManager: EntityManager, now: LocalDateTime): Long {
            val uketEventRounds =
                UketEventRandomUtil.createUketEventRounds(
                    listOf(now.minusDays(1), now),
                    now.minusDays(3),
                    now.minusDays(2)
                )

            val uketEvent =
                UketEventRandomUtil.createUketEvent(
                    uketEventRounds
                )

            entityManager.persist(uketEvent)
            entityManager.flush()

            return uketEvent.id
        }
    }
}
