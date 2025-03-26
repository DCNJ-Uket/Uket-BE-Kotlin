package uket.uket.domain.terms

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "term_sign")
class TermSign(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "terms_id", nullable = false)
    val terms: Terms,
    val usersId: Long,
    val isAgreed: Boolean,
    val agreeAt: LocalDateTime,
) : BaseTimeEntity()
