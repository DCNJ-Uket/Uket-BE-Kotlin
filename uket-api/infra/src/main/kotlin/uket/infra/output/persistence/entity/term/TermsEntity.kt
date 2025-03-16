package uket.infra.output.persistence.entity.term

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class TermsEntity(
    var name: String = "",
    var termsType: TermsType? = null,
    var documentNo: Long = 0L,
    var isActive: Boolean = false,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @Id
    @GeneratedValue
    @Column(name = "term_id")
    private var id: Long = 0L

}
