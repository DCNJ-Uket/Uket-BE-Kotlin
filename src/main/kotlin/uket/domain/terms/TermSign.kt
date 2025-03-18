package uket.uket.domain.terms

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity
import uket.uket.domain.user.Users
import java.time.LocalDateTime

@Entity
@Table(name = "term_sign")
class TermSign(
    id: Long,
    terms: Terms,
    usersId: Long,
    isAgreed: Boolean,
    agreeAt: LocalDateTime
) : BaseTimeEntity() {

    @Id @GeneratedValue
    @Column(name = "term_sign_id")
    var id: Long = id

    @ManyToOne
    @JoinColumn(name = "terms_id", nullable = false)
    var terms: Terms = terms

    @Column(nullable = false)
    var usersId: Long = usersId
        protected set

    @Column(nullable = false)
    var isAgreed: Boolean = isAgreed
        protected set

    @Column(nullable = false)
    var agreeAt: LocalDateTime = agreeAt
        protected set
}
