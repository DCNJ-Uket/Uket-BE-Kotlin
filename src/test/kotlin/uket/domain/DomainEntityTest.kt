package uket.domain

import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
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

    @Test
    @DisplayName("각 엔티티 생성 및 조회 테스트(누락 필드 확인)")
    fun test() {
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

        val organization = Organization(0L, "OrganiationA", null)
        val admin = Admin(0L, organization, "nameA", "emailA", "01012345678", "password123", true)

        val payment = Payment(0L, 0L, 0, "123-12-123457", "linkA", "nameA", "https:/123123")
        val paymentHistory =
            PaymentHistory(0L, 0L, 0L, 0, PaymentStatus.PURCHASED, "categoryA", PaymentManner.DEPOSIT_LINK, null)

        val ticket = Ticket(0L, 0L, 0L, TicketStatus.BEFORE_ENTER, "123456789", null)

        val document = Document(0L, 0L, "nameA", "linkA", 0L)
        val terms = Terms(0L, "nameA", TermsType.MANDATORY, 0L, true)
        val termSign = TermSign(0L, terms, document, 0L, true, LocalDateTime.now())

        val uketEventRound = UketEventRound(0L, null, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now())
        val uketEvent = UketEvent(
            id = 0L,
            organizationId = 0L,
            eventName = "nameA",
            eventType = EventType.FESTIVAL,
            location = "00시00구",
            totalTicketCount = 0,
            details = UketEvent.EventDetails(
                "", "", UketEvent.EventContact(UketEvent.EventContact.ContactType.INSTAGRAM, "@as", "")
            ),
            eventImageId = "",
            thumbnailImageId = "",
            _uketEventRounds = listOf(uketEventRound),
            _banners = listOf(),
        )
        val entryGroup = EntryGroup(0L, uketEventRound, "nameA", LocalDateTime.now(), LocalDateTime.now(), 0, 10)

        // when
        em.persist(user)

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
        em.flush()

        // then
        val findUser = em.find(User::class.java, user.id)
        println("findUsers id : ${findUser.id}")

        val findOrganization = em.find(Organization::class.java, organization.id)
        println("findOrganization id : ${findOrganization.id}")
        val findAdmin = em.find(Admin::class.java, admin.id)
        println("findAdmin id : ${findAdmin.id}")

        val findPayment = em.find(Payment::class.java, payment.id)
        println("findPayment id : ${findPayment.id}")
        val findPaymentHistory = em.find(PaymentHistory::class.java, paymentHistory.id)
        println("findPaymentHistory id : ${findPaymentHistory.id}")

        val findTicket = em.find(Ticket::class.java, ticket.id)
        println("findTicket id : ${findTicket.id}")

        val findDocument = em.find(Document::class.java, document.id)
        println("findDocument id : ${findDocument.id}")
        val findTerms = em.find(Terms::class.java, terms.id)
        println("findTerms id : ${findTerms.id}")
        val findTermSign = em.find(TermSign::class.java, termSign.id)
        println("findTermSign id : ${findTermSign.id}")

        val findUketEvent = em.find(UketEvent::class.java, uketEvent.id)
        println("findUketEvent id : ${findUketEvent.id}")
        val findUketEventRound = em.find(UketEventRound::class.java, uketEventRound.id)
        println("findUketEventRound id : ${findUketEventRound.id}")
        val findEntryGroup = em.find(EntryGroup::class.java, entryGroup.id)
        println("findEntryGroup id : ${findEntryGroup.id}")
    }
}
