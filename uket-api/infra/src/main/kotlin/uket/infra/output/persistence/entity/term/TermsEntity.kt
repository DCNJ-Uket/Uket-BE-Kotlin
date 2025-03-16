package uket.infra.output.persistence.entity.term

import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "term_id"))
class TermsEntity(
    var name: String = "",
    var termsType: TermsType? = null,
    var documentNo: Long = 0L,
    var isActive: Boolean = false
) : BaseTimeEntity() {
}
