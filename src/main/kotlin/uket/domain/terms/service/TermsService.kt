package uket.uket.domain.terms.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.uket.domain.terms.entity.Terms
import uket.uket.domain.terms.repository.TermsRepository

@Service
@Transactional(readOnly = true)
class TermsService(
    val termsRepository: TermsRepository,
) {
    fun findById(termsId: Long): Terms {
        val terms = termsRepository.findByIdOrNull(termsId)
            ?: throw IllegalStateException("약관을 찾을 수 없습니다.")
        return terms
    }

    fun findAllActive(): List<Terms> = termsRepository.findAllByIsActiveTrue()
}
