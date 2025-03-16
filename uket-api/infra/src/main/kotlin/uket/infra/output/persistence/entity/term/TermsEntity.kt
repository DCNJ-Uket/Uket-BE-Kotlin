package uket.infra.output.persistence.entity.term

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@Table(name = "terms")
@AttributeOverride(name = "id", column = Column(name = "term_id"))
class TermsEntity(
    name: String,
    termsType: TermsType,
    documentNo: Long,
    isActive: Boolean
) : BaseTimeEntity() {

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var termsType: TermsType = termsType
        protected set

    @Column(nullable = false)
    var documentNo: Long = documentNo
        protected set

    @Column(nullable = false)
    var isActive: Boolean = isActive
        protected set

}
