package uket.uket.domain.terms

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "term_sign")
class TermSign(
    _id: Long,
    _terms: Terms,
    val usersId: Long,
    val isAgreed: Boolean,
    val agreeAt: LocalDateTime,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "term_sign_id")
    val id: Long = _id

    @ManyToOne
    @JoinColumn(name = "terms_id", nullable = false)
    val terms: Terms = _terms
}
