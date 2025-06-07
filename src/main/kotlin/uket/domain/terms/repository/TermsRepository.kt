package uket.domain.terms.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.domain.terms.entity.Terms

interface TermsRepository : JpaRepository<Terms, Long> {
    fun findAllByIsActiveTrue(): List<Terms>

    fun findAllByIdIn(termIds: List<Long>): List<Terms>
}
