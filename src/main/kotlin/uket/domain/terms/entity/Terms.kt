package uket.domain.terms.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.common.PublicException
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
) : BaseTimeEntity() {
    fun checkMandatory(isAgreed: Boolean) {
        if (termsType !== TermsType.MANDATORY) {
            return
        }

        check(isAgreed) {
            throw PublicException(
                publicMessage = "서비스 이용을 위해선 필수 약관 동의가 필요합니다",
                systemMessage = "[Terms] 필수 약관은 거부할 수 없습니다",
                title = "필수 약관 거부"
            )
        }
    }
}
