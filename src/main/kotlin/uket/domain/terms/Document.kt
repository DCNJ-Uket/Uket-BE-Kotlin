package uket.uket.domain.terms

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "document")
@AttributeOverride(name = "id", column = Column(name = "document_id"))
class Document(
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
