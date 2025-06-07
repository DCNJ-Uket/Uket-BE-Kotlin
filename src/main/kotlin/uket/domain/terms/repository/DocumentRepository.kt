package uket.domain.terms.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.terms.entity.Document

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
        documentNos: List<Long>,
    ): List<Document>

    fun findAllByIdIn(documentIds: List<Long>): List<Document>
}
