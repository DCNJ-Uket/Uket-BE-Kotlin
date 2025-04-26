package uket.domain.terms.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.domain.BaseTimeEntity
import uket.domain.terms.enums.TermsType

@Entity
@Table(name = "terms")
class Terms(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val name: String,
    @Enumerated(EnumType.STRING)
    val termsType: TermsType,
    val documentNo: Long,
    val isActive: Boolean,
) : BaseTimeEntity()
