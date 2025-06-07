package uket.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.terms.dto.CheckRequiredTerms
import uket.domain.terms.dto.TermsAgreeAnswer
import uket.domain.terms.entity.TermSign
import uket.domain.terms.enums.TermsType
import uket.domain.terms.service.DocumentService
import uket.domain.terms.service.TermSignService
import uket.domain.terms.service.TermsService

@Service
class TermsDocumentFacade(
    private val termsService: TermsService,
    private val termSignService: TermSignService,
    private val documentService: DocumentService,
) {
    @Transactional(readOnly = true)
    fun getAllActiveAndCheckRequiredByUser(userId: Long): List<CheckRequiredTerms> {
        val activeTerms = termsService.findAllActive()

        val documentsMap = documentService.getDocumentMapByDocumentNo(activeTerms)
        val termSignMap = termSignService.getLatestTermSignMap(activeTerms, userId)

        val checkRequiredTerms = activeTerms
            .filter {
                val latestDocument = documentsMap[it.documentNo]
                    ?: throw IllegalStateException("해당 documentNo에 대한 document가 존재하지 않습니다.")
                val termSign = termSignMap[it.id]
                // 약관에 대한 동의 여부가 없거나 최신 약관문에 대한 동의 여부가 없는 경우
                termSign == null || termSign.document.version < latestDocument.version
            }.map {
                val latestDocument = documentsMap[it.documentNo]!!
                CheckRequiredTerms.of(it, latestDocument)
            }

        return checkRequiredTerms.sortedWith(mandatoryFirstComparator())
    }

    private fun mandatoryFirstComparator(): Comparator<CheckRequiredTerms> = compareBy {
        when (it.termsType) {
            TermsType.MANDATORY -> 1
            else -> 2
        }
    }

    @Transactional
    fun agreeTerms(userId: Long, agreeAnswers: List<TermsAgreeAnswer>): List<TermSign> {
        val termIds = agreeAnswers.map { it.termsId }
        val termsMap = termsService.findAllByIdIn(termIds).associateBy { it.id }
        val documentIds = agreeAnswers.map { it.documentId }
        val documentMap = documentService.findAllByIdIn(documentIds).associateBy { it.id }

        val termsSigns: List<TermSign> = agreeAnswers
            .map { answer ->
                val termsId = answer.termsId
                val isAgreed = answer.isAgree
                val documentId = answer.documentId

                val term = termsMap[termsId] ?: throw IllegalStateException("약관을 찾을 수 없습니다.")
                term.checkMandatory(isAgreed)

                val document = documentMap[documentId] ?: throw IllegalStateException("약관문을 찾을 수 없습니다.")
                if (isAgreed) TermSign.agree(userId, term, document) else TermSign.agreeNot(userId, term, document)
            }

        termSignService.batchSaveAllWithJdbc(termsSigns)
        return termSignService.findByUserIdAndIdsIn(termIds, userId)
    }
}
