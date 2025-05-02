package uket.domain.terms.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "term_sign")
class TermSign(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id")
    val terms: Terms,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    val document: Document,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "is_agreed")
    val isAgreed: Boolean,

    @Column(name = "agree_at")
    val agreeAt: LocalDateTime,
) : BaseTimeEntity() {
    companion object {
        fun agree(userId: Long, terms: Terms, document: Document): TermSign = TermSign(
            userId = userId,
            terms = terms,
            isAgreed = true,
            document = document,
            agreeAt = LocalDateTime.now()
        )

        fun agreeNot(userId: Long, terms: Terms, document: Document): TermSign = TermSign(
            userId = userId,
            terms = terms,
            isAgreed = false,
            document = document,
            agreeAt = LocalDateTime.now()
        )
    }
}
