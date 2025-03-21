package uket.domain

import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import uket.uket.domain.organization.Admin
import uket.uket.domain.organization.Organization
import uket.uket.domain.payment.Payment
import uket.uket.domain.payment.PaymentHistory
import uket.uket.domain.payment.PaymentManner
import uket.uket.domain.payment.PaymentStatus
import uket.uket.domain.reservation.Ticket
import uket.uket.domain.reservation.TicketStatus
import uket.uket.domain.terms.Document
import uket.uket.domain.terms.TermSign
import uket.uket.domain.terms.Terms
import uket.uket.domain.terms.TermsType
import uket.uket.domain.uketevent.EntryGroup
import uket.uket.domain.uketevent.EventType
import uket.uket.domain.uketevent.UketEvent
import uket.uket.domain.uketevent.UketEventRound
import uket.uket.domain.user.Platform
import uket.uket.domain.user.Users
import java.time.LocalDateTime

@DataJpaTest
class DomainEntityTest {
    @Autowired
    lateinit var em: EntityManager

    @Test
    @DisplayName("테스트 디비와 연동이 잘 되는 지 확인")
    fun testDBIntegration() {
        // given
        val users = Users(0L, Platform.KAKAO, 0L, "nameA", "emailA", null, "depositorNameA", "phoneNumberA", true)
        em.persist(users)

        // when
        val findUsers = em.find(Users::class.java, users.id)

        // then
        Assertions.assertThat(findUsers.id).isEqualTo(users.id)
    }

    @Test
    @DisplayName("각 엔티티 생성 및 조회 테스트(누락 필드 확인)")
    fun test() {
        // given
        val users = Users(0L, Platform.KAKAO, 0L, "nameA", "emailA", null, "depositorNameA", "phoneNumberA", true)

        val organization = Organization(0L, "OrganiationA", null)
        val admin = Admin(0L, organization, "nameA", "emailA", "password123", true)

        val payment = Payment(0L, 0L, "123-12-123457", "linkA")
        val paymentHistory = PaymentHistory(0L, 0L, 0L, 0, PaymentStatus.PURCHASED, "categoryA", PaymentManner.DEPOSIT_LINK, null)

        val ticket = Ticket(0L, 0L, 0L, TicketStatus.BEFORE_ENTER, "123456789")

        val document = Document(0L, 0L, "nameA", "linkA", 0L)
        val terms = Terms(0L, "nameA", TermsType.MANDATORY, 0L, true)
        val termSign = TermSign(0L, terms, 0L, true, LocalDateTime.now())

        val uketEvent = UketEvent(0L, 0L, "nameA", EventType.FESTIVAL, "00시00구", null, LocalDateTime.now(), 0)
        val uketEventRound = UketEventRound(0L, uketEvent, "nameA", LocalDateTime.now(), LocalDateTime.now())
        val entryGroup = EntryGroup(0L, uketEventRound, "nameA", LocalDateTime.now(), LocalDateTime.now(), 0, 100)

        // when
        em.persist(users)

        em.persist(organization)
        em.persist(admin)

        em.persist(payment)
        em.persist(paymentHistory)

        em.persist(ticket)

        em.persist(document)
        em.persist(terms)
        em.persist(termSign)

        em.persist(uketEvent)
        em.persist(uketEventRound)
        em.persist(entryGroup)

        // then
        val findUsers = em.find(Users::class.java, 1L)
        println("findUsers id : ${findUsers.id}")

        val findOrganization = em.find(Organization::class.java, 1L)
        println("findOrganization id : ${findOrganization.id}")
        val findAdmin = em.find(Admin::class.java, 1L)
        println("findAdmin id : ${findAdmin.id}")

        val findPayment = em.find(Payment::class.java, 1L)
        println("findPayment id : ${findPayment.id}")
        val findPaymentHistory = em.find(PaymentHistory::class.java, 1L)
        println("findPaymentHistory id : ${findPaymentHistory.id}")

        val findTicket = em.find(Ticket::class.java, 1L)
        println("findTicket id : ${findTicket.id}")

        val findDocument = em.find(Document::class.java, 1L)
        println("findDocument id : ${findDocument.id}")
        val findTerms = em.find(Terms::class.java, 1L)
        println("findTerms id : ${findTerms.id}")
        val findTermSign = em.find(TermSign::class.java, 1L)
        println("findTermSign id : ${findTermSign.id}")

        val findUketEvent = em.find(UketEvent::class.java, 1L)
        println("findUketEvent id : ${findUketEvent.id}")
        val findUketEventRound = em.find(UketEventRound::class.java, 1L)
        println("findUketEventRound id : ${findUketEventRound.id}")
        val findEntryGroup = em.find(EntryGroup::class.java, 1L)
        println("findEntryGroup id : ${findEntryGroup.id}")
    }
}
