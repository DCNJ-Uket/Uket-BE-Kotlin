package uket.uket.domain.terms.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import uket.uket.domain.terms.entity.TermSign

interface TermSignRepository : JpaRepository<TermSign, Long> {
    @Query(
        """
        SELECT ts
        FROM TermSign ts
        WHERE ts.userId = :userId
          AND ts.termsId IN :termsIds
          AND ts.agreeAt = (
              SELECT MAX(subTs.agreeAt)
              FROM TermSign subTs
              WHERE subTs.termsId = ts.termsId AND subTs.userId = ts.userId
          )
    """,
    )
    fun findLatestByUserIdAndTermsIds(
        @Param("userId") userId: Long?,
        @Param("termsIds") termsIds: List<Long?>?,
    ): List<TermSign>
}
