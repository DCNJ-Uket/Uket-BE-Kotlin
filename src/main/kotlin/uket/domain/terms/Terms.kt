package uket.uket.domain.terms

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "terms")
class Terms(
    id: Long,
    name: String,
    termsType: TermsType,
    documentNo: Long,
    isActive: Boolean
) : BaseTimeEntity() {

    @Id @GeneratedValue
    @Column(name = "terms_id")
    var id: Long = id

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
