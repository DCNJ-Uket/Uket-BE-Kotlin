package uket.uket.domain.terms

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "document")
class Document(
    _id: Long,
    val documentNo: Long,
    val name: String,
    val link: String,
    val version: Long,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "document_id")
    val id: Long = _id
}
