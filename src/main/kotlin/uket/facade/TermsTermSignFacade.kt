package uket.facade

import org.springframework.stereotype.Service
import uket.domain.terms.dto.OptionalTermAnswer
import uket.domain.terms.service.TermSignService
import uket.domain.terms.service.TermsService

@Service
class TermsTermSignFacade(
    private val termsService: TermsService,
    private val termSignService: TermSignService,
) {
    fun getAllActiveAndOptionalUserAnswers(userId: Long): List<OptionalTermAnswer> {
        val optionalTerms = termsService.findAllActiveOptional()
        val termSignMap = termSignService.getLatestTermSignMap(optionalTerms, userId)

        return optionalTerms.map { terms ->
            val termSign = termSignMap[terms.id]!!
            OptionalTermAnswer.of(terms, termSign)
        }
    }
}
