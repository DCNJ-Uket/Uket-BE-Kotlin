package uket.infra.output.persistence.entity.term

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
class TermSignEntity(
    var useId: Long = 0L,
    var termId: Long = 0L,
    var isAgreed: Boolean = false,
    var agreeAt: LocalDateTime = LocalDateTime.now()
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "term_sign_id")
    private var id: Long = 0L

}
