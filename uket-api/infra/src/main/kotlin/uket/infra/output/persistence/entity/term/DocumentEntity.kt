package uket.infra.output.persistence.entity.term

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "document_id"))
class DocumentEntity(
    var documentNo: Long = 0L,
    var name: String = "",
    var link: String = "",
    var version: Long = 0L
) : BaseTimeEntity() {
}
