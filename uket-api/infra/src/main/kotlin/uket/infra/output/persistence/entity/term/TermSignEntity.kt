package uket.infra.output.persistence.entity.term

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "term_sign_id"))
class TermSignEntity(
    var useId: Long = 0L,
    var termId: Long = 0L,
    var isAgreed: Boolean = false,
    var agreeAt: LocalDateTime = LocalDateTime.now()
) : BaseTimeEntity() {
}
