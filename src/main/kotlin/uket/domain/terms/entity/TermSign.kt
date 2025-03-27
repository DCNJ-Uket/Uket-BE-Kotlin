package uket.domain.terms.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "term_sign")
class TermSign(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val termsId: Long,
    val userId: Long,
    val isAgreed: Boolean,
    val agreeAt: LocalDateTime,
) : BaseTimeEntity()
