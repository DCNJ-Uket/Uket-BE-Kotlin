package uket.domain.terms.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.terms.entity.Document
import uket.domain.terms.entity.Terms
import uket.domain.terms.repository.DocumentRepository

@Service
@Transactional(readOnly = true)
class DocumentService(
    private val documentRepository: DocumentRepository,
) {
    @Transactional(readOnly = true)
    fun getDocumentMapByDocumentNo(activeTerms: List<Terms>): Map<Long, Document> {
        val activeDocumentNos = activeTerms.map { it.documentNo }
        val latestDocumentsMap = documentRepository
            .findLatestDocumentsByDocumentNos(activeDocumentNos)
            .associateBy { it.documentNo }
        return latestDocumentsMap
    }

    @Transactional(readOnly = true)
    fun findAllByIdIn(documentIds: List<Long>): List<Document> = documentRepository.findAllByIdIn(documentIds)
}
