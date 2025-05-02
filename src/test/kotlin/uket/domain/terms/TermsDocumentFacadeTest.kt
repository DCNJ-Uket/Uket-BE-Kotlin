package uket.domain.terms

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import uket.common.PublicException
import uket.domain.terms.entity.Document
import uket.domain.terms.entity.TermSign
import uket.domain.terms.entity.Terms
import uket.domain.terms.enums.TermsType
import uket.domain.terms.repository.DocumentRepository
import uket.domain.terms.repository.TermSignRepository
import uket.domain.terms.repository.TermsRepository
import uket.domain.terms.service.DocumentService
import uket.domain.terms.service.TermSignService
import uket.domain.terms.service.TermsService
import uket.uket.domain.terms.dto.TermsAgreeAnswer
import uket.uket.domain.terms.repository.TermSignJdbcRepository
import uket.uket.facade.TermsDocumentFacade
import java.time.LocalDateTime

class TermsDocumentFacadeTest :
    DescribeSpec({
        isolationMode = IsolationMode.InstancePerLeaf

        val termsRepository = mockk<TermsRepository>()
        val termSignRepository = mockk<TermSignRepository>()
        val documentRepository = mockk<DocumentRepository>()
        val termSignJdbcRepository = mockk<TermSignJdbcRepository>()
        val termsService = TermsService(termsRepository)
        val termSignService = TermSignService(termSignRepository, termSignJdbcRepository)
        val documentService = DocumentService(documentRepository)
        val termsDocumentFacade = TermsDocumentFacade(termsService, termSignService, documentService)

        val terms = Terms(1L, "필수 약관", TermsType.MANDATORY, 1L, true)
        val document = Document(1L, 1L, "문서1", "samplelink", 1L)
        val userId = 1L

        describe("agreeTerms") {
            context("필수 약관 1개가 있을 때") {
                val userAnswerYes = listOf(TermsAgreeAnswer(terms.id, true, document.version))
                every { termsRepository.findAllByIdIn(listOf(terms.id)) } returns listOf(terms)
                every { documentRepository.findAllByIdIn(listOf(document.id)) } returns listOf(document)

                val slot = slot<List<TermSign>>()
                every { termSignJdbcRepository.batchSaveAll(capture(slot)) } returns Unit
                every { termSignRepository.findLatestByUserIdAndTermsIdsWithTermsAndDocument(userId, listOf(terms.id)) } returns listOf()

                it("유저가 동의 요청을 제출하면 TermSign 객체가 생성됨") {
                    val returnedTermSigns = termsDocumentFacade.agreeTerms(userId, userAnswerYes)
                    val termSigns = slot.captured
                    termSigns.size shouldBe 1
                    termSigns[0].userId shouldBe userId
                    termSigns[0].document.id shouldBe document.id
                    termSigns[0].isAgreed shouldBe true
                }

                val userAnswerNo = listOf(TermsAgreeAnswer(terms.id, false, document.version))
                it("유저가 거부 요청을 제출하면 에러 발생") {
                    val exception = shouldThrow<PublicException> { termsDocumentFacade.agreeTerms(userId, userAnswerNo) }
                    exception.title shouldBe "필수 약관 거부"
                }
            }
        }

        describe("getAllActiveAndCheckRequiredByUser") {
            context("약관이 하나 있고, 유저가 해당 약관을 확인한 내역이 없는 경우") {
                every { termsRepository.findAllByIsActiveTrue() } returns listOf(terms)
                every { termSignRepository.findLatestByUserIdAndTermsIdsWithTermsAndDocument(userId, listOf(1L)) } returns listOf()
                every { documentRepository.findLatestDocumentsByDocumentNos(listOf(terms.documentNo)) } returns listOf(document)

                it("해당 약관을 포함한 목록 제공") {
                    val checkRequiredTerms = termsDocumentFacade.getAllActiveAndCheckRequiredByUser(userId)
                    checkRequiredTerms.size shouldBe 1
                    checkRequiredTerms[0].termsId shouldBe terms.id
                    checkRequiredTerms[0].name shouldBe terms.name
                    checkRequiredTerms[0].termsType shouldBe terms.termsType
                    checkRequiredTerms[0].link shouldBe document.link
                    checkRequiredTerms[0].documentVersion shouldBe document.version
                }
            }

            context("약관이 하나 있고, 유저가 해당 약관을 확인한 내역이 있는 경우") {
                val termSign = TermSign(1L, terms, document, userId, true, LocalDateTime.now())

                every { termsRepository.findAllByIsActiveTrue() } returns listOf(terms)
                every { termSignRepository.findLatestByUserIdAndTermsIdsWithTermsAndDocument(userId, listOf(1L)) } returns listOf(termSign)
                every { documentRepository.findLatestDocumentsByDocumentNos(listOf(terms.documentNo)) } returns listOf(document)

                it("해당 약관을 포함한 목록 제공") {
                    val checkRequiredTerms = termsDocumentFacade.getAllActiveAndCheckRequiredByUser(userId)
                    checkRequiredTerms.size shouldBe 0
                }
            }

            context("약관이 하나 있고, 유저가 해당 약관에 동의를 했었지만, 새로운 약관문이 추가된 경우") {
                val newDocument = Document(2L, 1L, "문서2", "samplelink2", 2L)
                val termSign = TermSign(1L, terms, document, userId, true, LocalDateTime.now())

                every { termsRepository.findAllByIsActiveTrue() } returns listOf(terms)
                every { termSignRepository.findLatestByUserIdAndTermsIdsWithTermsAndDocument(userId, listOf(1L)) } returns listOf(termSign)
                every { documentRepository.findLatestDocumentsByDocumentNos(listOf(terms.documentNo)) } returns listOf(newDocument)

                it("해당 약관을 포함한 목록 제공") {
                    val checkRequiredTerms = termsDocumentFacade.getAllActiveAndCheckRequiredByUser(userId)
                    checkRequiredTerms.size shouldBe 1
                    checkRequiredTerms[0].termsId shouldBe terms.id
                    checkRequiredTerms[0].name shouldBe terms.name
                    checkRequiredTerms[0].termsType shouldBe terms.termsType
                    checkRequiredTerms[0].link shouldBe newDocument.link
                    checkRequiredTerms[0].documentVersion shouldBe newDocument.version
                }
            }
        }
    })
