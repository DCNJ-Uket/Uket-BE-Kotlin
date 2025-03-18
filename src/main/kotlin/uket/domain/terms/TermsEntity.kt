package uket.uket.domain.terms

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

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
