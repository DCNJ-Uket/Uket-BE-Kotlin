package uket.uket.domain.terms.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.uket.domain.terms.entity.TermSign
import uket.uket.domain.terms.repository.TermSignRepository
import java.util.stream.Collectors

@Service
@Transactional(readOnly = true)
class TermSignService(
    val termSignRepository: TermSignRepository,
) {
    fun findById(termSignId: Long): TermSign {
        val termSign = termSignRepository.findByIdOrNull(termSignId)
            ?: throw IllegalStateException("해당 약관 동의 내역을 찾을 수 없습니다")
        return termSign
    }

    fun getTermsSignMap(userId: Long?, termsIds: List<Long?>?): Map<Long, Boolean> = termSignRepository
        .findLatestByUserIdAndTermsIds(
            userId,
            termsIds,
        ).stream()
        .collect(Collectors.toMap(TermSign::termsId, TermSign::isAgreed))

    @Transactional
    fun saveAll(termsSigns: List<TermSign>): List<TermSign> = termSignRepository.saveAll(termsSigns)
}
