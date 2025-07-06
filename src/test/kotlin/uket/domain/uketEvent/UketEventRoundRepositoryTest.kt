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
import uket.domain.uketevent.repository.UketEventRepository
import uket.domain.uketevent.repository.UketEventRoundRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@DataJpaTest
class UketEventRoundRepositoryTest(
    private val uketEventRepository: UketEventRepository,
    private val uketEventRoundRepository: UketEventRoundRepository,
    private val entityManager: EntityManager,
) : DescribeSpec({

        isolationMode = IsolationMode.InstancePerTest

        describe("findByUketEventIdAAndEventRoundDateAfterNow") {
            context("회차가 2개 있고, 하나는 어제일 때") {
                it("1개의 회차 조회") {
                    val now = LocalDateTime.now()
                    val savedUketEvent = uketEventRepository.save(UketEventRandomUtil.createUketEvent())

                    val uketEventRounds = List(2) { index ->
                        UketEventRandomUtil.createUketEventRound(
                            savedUketEvent,
                            now.minusDays(index.toLong()),
                            now.minusDays(3),
                            now.minusDays(2)
                        )
                    }
                    uketEventRoundRepository.saveAll(uketEventRounds)

                    val savedUketEventRounds = uketEventRoundRepository.findByUketEventIdAndEventRoundDateAfter(
                        savedUketEvent.id,
                        now.truncatedTo(
                            ChronoUnit.DAYS
                        )
                    )
                    savedUketEventRounds.size shouldBe 1
                    savedUketEventRounds[0].eventRoundDateTime.toLocalDate() shouldBe after(LocalDate.now().minusDays(1))
                }
            }
        }
    }
    ) {
    override fun extensions(): List<Extension> {
        return super.extensions() + listOf(SpringExtension) // SpringExtension 활성화
    }
}
