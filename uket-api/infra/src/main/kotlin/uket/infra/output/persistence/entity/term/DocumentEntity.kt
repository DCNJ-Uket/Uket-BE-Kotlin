package uket.infra.output.persistence.entity.term

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "document")
@AttributeOverride(name = "id", column = Column(name = "document_id"))
class DocumentEntity(
    documentNo: Long,
    name: String,
    link: String,
    version: Long
) : BaseTimeEntity() {

    @Column(nullable = false)
    var documentNo: Long = documentNo
        protected set

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var link: String = link
        protected set

    @Column(nullable = false)
    var version: Long = version
        protected set
}
