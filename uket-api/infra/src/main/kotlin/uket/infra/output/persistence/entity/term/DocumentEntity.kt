package uket.infra.output.persistence.entity.term

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "document_id"))
class DocumentEntity(
    documentNo: Long,
    name: String,
    link: String,
    version: Long
) : BaseTimeEntity() {
    var documentNo: Long = documentNo
        protected set
    var name: String = name
        protected set
    var link: String = link
        protected set
    var version: Long = version
        protected set
}
