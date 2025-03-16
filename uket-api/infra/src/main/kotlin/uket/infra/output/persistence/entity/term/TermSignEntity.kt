package uket.infra.output.persistence.entity.term

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import uket.infra.output.persistence.entity.user.UserEntity
import java.time.LocalDateTime

@Entity
@Table(name = "term_sign")
@AttributeOverride(name = "id", column = Column(name = "term_sign_id"))
class TermSignEntity(
    userEntity: UserEntity,
    termId: Long,
    isAgreed: Boolean,
    agreeAt: LocalDateTime
) : BaseTimeEntity() {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var userEntity: UserEntity = userEntity
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
