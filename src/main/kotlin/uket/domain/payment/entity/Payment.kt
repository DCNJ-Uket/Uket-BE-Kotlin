package uket.domain.payment.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.common.enums.BankCode
import uket.domain.BaseTimeEntity

@Entity
@Table(name = "payment")
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(name = "organization_id", unique = true)
    val organizationId: Long,
    @Embedded
    val account: Account,
    @Column(name = "deposit_link")
    val depositLink: String,
) : BaseTimeEntity() {
    @Embeddable
    data class Account(
        @Enumerated(EnumType.STRING)
        @Column(name = "bank_code")
        val bankCode: BankCode,

        @Column(name = "account_number")
        val accountNumber: String,

        @Column(name = "depositor_name")
        val depositorName: String,
    )
}
