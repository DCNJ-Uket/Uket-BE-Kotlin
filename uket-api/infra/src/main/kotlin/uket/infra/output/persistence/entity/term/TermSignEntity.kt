package uket.infra.output.persistence.entity.term

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "term_sign_id"))
class TermSignEntity(
    userId: Long,
    termId: Long,
    isAgreed: Boolean,
    agreeAt: LocalDateTime
) : BaseTimeEntity() {
    var useId: Long = userId
        protected set

    var termId: Long = termId
        protected set

    var isAgreed: Boolean = isAgreed
        protected set

    var agreeAt: LocalDateTime = agreeAt
        protected set
}
