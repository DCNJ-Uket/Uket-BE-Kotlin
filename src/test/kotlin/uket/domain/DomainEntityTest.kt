package uket.domain

import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import uket.common.enums.BankCode
import uket.common.enums.EventType
import uket.domain.admin.entity.Admin
import uket.domain.admin.entity.Organization
import uket.domain.payment.entity.Payment
import uket.domain.payment.entity.PaymentHistory
import uket.domain.payment.enums.PaymentManner
import uket.domain.payment.enums.PaymentStatus
import uket.domain.reservation.entity.Ticket
import uket.domain.reservation.enums.TicketStatus
import uket.domain.terms.entity.Document
import uket.domain.terms.entity.TermSign
import uket.domain.terms.entity.Terms
import uket.domain.terms.enums.TermsType
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import uket.domain.user.entity.User
import uket.domain.user.enums.Platform
import java.time.LocalDateTime

@DataJpaTest
class DomainEntityTest {
    @Autowired
    lateinit var em: EntityManager

    @Test
    @DisplayName("테스트 디비와 연동이 잘 되는 지 확인")
    fun testDBIntegration() {
        // given
        val user = User(
            0L,
            Platform.KAKAO,
            "platformIdA",
            "nameA",
            "emailA",
            "profileImageA",
            "depositorNameA",
            "phoneNumberA",
            true
        )
        em.persist(user)

        // when
        val findUser = em.find(User::class.java, user.id)

        // then
        Assertions.assertThat(findUser.id).isEqualTo(user.id)
    }
}
