package uket.domain.terms.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.terms.entity.Document
import uket.domain.terms.repository.DocumentRepository
import java.util.stream.Collectors

@Service
@Transactional(readOnly = true)
class DocumentService(
    private val documentRepository: DocumentRepository,
) {
    fun getLinkMap(documentNos: List<Long>): Map<Long, String> = documentRepository
        .findLatestDocumentsByDocumentNos(documentNos)
        .stream()
        .collect(
            Collectors.toMap(
                Document::documentNo,
                Document::link,
            ),
        )
}
