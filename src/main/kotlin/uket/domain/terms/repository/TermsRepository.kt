package uket.uket.domain.terms.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.uket.domain.terms.entity.Terms

interface TermsRepository : JpaRepository<Terms, Long> {
    fun findAllByIsActiveTrue(): List<Terms>
}
