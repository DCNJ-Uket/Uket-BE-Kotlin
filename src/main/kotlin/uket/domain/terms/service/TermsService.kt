package uket.domain.terms.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.terms.entity.Terms
import uket.domain.terms.enums.TermsType
import uket.domain.terms.repository.TermsRepository

@Service
class TermsService(
    private val termsRepository: TermsRepository,
) {
    @Transactional(readOnly = true)
    fun getById(termsId: Long): Terms {
        val terms = termsRepository.findByIdOrNull(termsId)
            ?: throw IllegalStateException("약관을 찾을 수 없습니다.")
        return terms
    }

    @Transactional(readOnly = true)
    fun findAllActive(): List<Terms> = termsRepository.findAllByIsActiveTrue()

    @Transactional(readOnly = true)
    fun findAllActiveOptional(): List<Terms> {
        val activeTerms = termsRepository.findAllByIsActiveTrue()
        return activeTerms.filter { it.termsType == TermsType.OPTIONAL }
    }

    @Transactional(readOnly = true)
    fun findAllByIdIn(termIds: List<Long>): List<Terms> = termsRepository.findAllByIdIn(termIds)
}
