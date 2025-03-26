package uket.uket.domain.terms.entity

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "document")
class Document(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val documentNo: Long,
    val name: String,
    val link: String,
    val version: Long,
) : BaseTimeEntity()
