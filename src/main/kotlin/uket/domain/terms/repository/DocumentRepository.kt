package uket.uket.domain.terms.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import uket.uket.domain.terms.entity.Document

interface DocumentRepository : JpaRepository<Document, Long> {
    @Query(
        """
        SELECT d
        FROM Document d
        WHERE d.documentNo IN :documentNos
          AND d.version = (
              SELECT MAX(subD.version)
              FROM Document subD
              WHERE subD.documentNo = d.documentNo
          )
    """,
    )
    fun findLatestDocumentsByDocumentNos(
        @Param("documentNos") documentNos: List<Long>,
    ): List<Document>
}
