package uket.uket.domain.terms

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity
import uket.uket.domain.user.UsersEntity
import java.time.LocalDateTime

@Entity
@Table(name = "term_sign")
@AttributeOverride(name = "id", column = Column(name = "term_sign_id"))
class TermSignEntity(
    usersEntity: UsersEntity,
    termId: Long,
    isAgreed: Boolean,
    agreeAt: LocalDateTime
) : BaseTimeEntity() {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var usersEntity: UsersEntity = usersEntity
        protected set

    @Column(nullable = false)
    var termId: Long = termId
        protected set

    @Column(nullable = false)
    var isAgreed: Boolean = isAgreed
        protected set

    @Column(nullable = false)
    var agreeAt: LocalDateTime = agreeAt
        protected set
}
