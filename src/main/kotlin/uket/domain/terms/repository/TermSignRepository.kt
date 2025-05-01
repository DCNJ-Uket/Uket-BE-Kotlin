package uket.domain.terms.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.terms.entity.TermSign

interface TermSignRepository : JpaRepository<TermSign, Long> {
    @Query(
        """
            SELECT ts
            FROM TermSign ts
            JOIN FETCH ts.terms
            JOIN FETCH ts.document
            WHERE ts.userId = :userId
              AND ts.terms.id IN :termIds
              AND ts.agreeAt = (
                  SELECT MAX(subTs.agreeAt)
                  FROM TermSign subTs
                  WHERE subTs.terms.id = ts.terms.id AND subTs.userId = ts.userId
              )
    
        """
    )
    fun findLatestByUserIdAndTermsIdsWithTermsAndDocument(
        userId: Long,
        termIds: List<Long>,
    ): List<TermSign>
}
