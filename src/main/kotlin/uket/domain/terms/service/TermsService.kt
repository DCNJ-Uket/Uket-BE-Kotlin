package uket.domain.terms.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.terms.entity.Document
import uket.domain.terms.entity.TermSign
import uket.domain.terms.entity.Terms
import uket.domain.terms.repository.DocumentRepository
import uket.domain.terms.repository.TermSignRepository
import uket.domain.terms.repository.TermsRepository
import uket.uket.domain.terms.dto.CheckRequiredTerms
import uket.uket.domain.terms.dto.TermsAgreeAnswer

@Service
class TermsService(
    private val termsRepository: TermsRepository,
    private val termSignRepository: TermSignRepository,
    private val documentRepository: DocumentRepository,
) {
    private final val MIN_VERSION = -1L

    @Transactional(readOnly = true)
    fun getById(termsId: Long): Terms {
        val terms = termsRepository.findByIdOrNull(termsId)
            ?: throw IllegalStateException("약관을 찾을 수 없습니다.")
        return terms
    }

    @Transactional
    fun agreeTerms(userId: Long, agreeAnswers: List<TermsAgreeAnswer>): List<TermSign> {
        val termIds = agreeAnswers.map { it.termsId }
        val termsMap = termsRepository.findAllByIdIn(termIds).associateBy { it.id }

        val termsSigns: List<TermSign> = agreeAnswers
            .map { answer ->
                val termsId = answer.termsId
                val isAgreed = answer.isAgree
                val documentVersion = answer.documentVersion

                val term = termsMap[termsId] ?: throw IllegalStateException("약관을 찾을 수 없습니다.")
                term.checkMandatory(isAgreed)

                if (isAgreed) TermSign.agree(userId, term, documentVersion) else TermSign.agreeNot(userId, term, documentVersion)
            }

        return termSignRepository.saveAll(termsSigns)
    }

    @Transactional(readOnly = true)
    fun getAllActiveAndCheckRequiredByUser(userId: Long): List<CheckRequiredTerms> {
        val activeTerms = termsRepository.findAllByIsActiveTrue()

        val documentsMap = getDocumentsMap(activeTerms)
        val termSignMap = getTermSignMap(activeTerms, userId)

        return activeTerms.mapNotNull {
            val latestDocument = documentsMap[it.documentNo] ?: throw IllegalStateException("해당 documentNo에 대한 document가 존재하지 않습니다.")

            // 약관에 대한 동의 여부가 없는 경우
            val termSign = termSignMap[it.id] ?: return@mapNotNull CheckRequiredTerms.of(it, latestDocument)

            // 최신 약관문에 대한 동의 여부가 없는 경우
            if (termSign.documentVersion < latestDocument.version) {
                return@mapNotNull CheckRequiredTerms.of(it, latestDocument)
            }
            return@mapNotNull null
        }
    }

    private fun getTermSignMap(
        activeTerms: List<Terms>,
        userId: Long,
    ): Map<Long, TermSign> {
        val activeTermIds = activeTerms.map { it.id }
        val termSignVersionsMap = termSignRepository
            .findLatestByUserIdAndTermsIdsWithTerms(userId, activeTermIds)
            .associateBy { it.terms.id }
        return termSignVersionsMap
    }

    private fun getDocumentsMap(activeTerms: List<Terms>): Map<Long, Document> {
        val activeDocumentNos = activeTerms.map { it.documentNo }
        val latestDocumentsMap = documentRepository
            .findLatestDocumentsByDocumentNos(activeDocumentNos)
            .associateBy { it.documentNo }
        return latestDocumentsMap
    }
}
