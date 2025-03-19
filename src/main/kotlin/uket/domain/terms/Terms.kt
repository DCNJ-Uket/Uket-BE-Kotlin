package uket.uket.domain.terms

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "terms")
class Terms(
    _id: Long,
    val name: String,
    val termsType: TermsType,
    val documentNo: Long,
    val isActive: Boolean,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "terms_id")
    val id: Long = _id
}
