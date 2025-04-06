package uket.domain.terms.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.domain.BaseTimeEntity

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
