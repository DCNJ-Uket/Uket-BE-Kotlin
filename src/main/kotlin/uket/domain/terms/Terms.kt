package uket.uket.domain.terms

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

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
