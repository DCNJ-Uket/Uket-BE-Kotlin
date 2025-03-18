package uket.uket.domain.terms

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "document")
class Document(
    id: Long,
    documentNo: Long,
    name: String,
    link: String,
    version: Long
) : BaseTimeEntity() {

    @Id @GeneratedValue
    @Column(name = "document_id")
    var id: Long = id

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
