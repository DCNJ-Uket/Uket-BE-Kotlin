package uket.domain.terms.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.terms.repository.DocumentRepository

@Service
@Transactional(readOnly = true)
class DocumentService(
    private val documentRepository: DocumentRepository,
)
