package uket.domain.terms.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.terms.entity.TermSign
import uket.domain.terms.entity.Terms
import uket.domain.terms.repository.TermSignRepository
import uket.uket.domain.terms.repository.TermSignJdbcRepository

@Service
@Transactional(readOnly = true)
class TermSignService(
    private val termSignRepository: TermSignRepository,
    private val termSignJdbcRepository: TermSignJdbcRepository,
) {
    @Transactional(readOnly = true)
    fun getById(termSignId: Long): TermSign {
        val termSign = termSignRepository.findByIdOrNull(termSignId)
            ?: throw IllegalStateException("해당 약관 동의 내역을 찾을 수 없습니다")
        return termSign
    }

    @Transactional
    fun batchSaveAllWithJdbc(termsSigns: List<TermSign>) =
        termSignJdbcRepository.batchSaveAll(termsSigns)

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

    @Transactional(readOnly = true)
    fun findByUserIdAndIdsIn(
        termIds: List<Long>,
        userId: Long,
    ): List<TermSign> = termSignRepository
        .findLatestByUserIdAndTermsIdsWithTermsAndDocument(userId, termIds)
}
