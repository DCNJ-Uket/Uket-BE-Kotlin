package uket.domain.terms.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.terms.entity.TermSign
import uket.domain.terms.entity.Terms
import uket.domain.terms.repository.TermSignRepository

@Service
@Transactional(readOnly = true)
class TermSignService(
    private val termSignRepository: TermSignRepository,
) {
    @Transactional(readOnly = true)
    fun getById(termSignId: Long): TermSign {
        val termSign = termSignRepository.findByIdOrNull(termSignId)
            ?: throw IllegalStateException("해당 약관 동의 내역을 찾을 수 없습니다")
        return termSign
    }

    @Transactional
    fun saveAll(termsSigns: List<TermSign>): List<TermSign> = termSignRepository.saveAll(termsSigns)

    @Transactional(readOnly = true)
    fun getLatestTermSignMap(
        activeTerms: List<Terms>,
        userId: Long,
    ): Map<Long, TermSign> {
        val activeTermIds = activeTerms.map { it.id }
        val termSignVersionsMap = termSignRepository
            .findLatestByUserIdAndTermsIdsWithTermsAndDocument(userId, activeTermIds)
            .associateBy { it.terms.id }
        return termSignVersionsMap
    }
}
