package uket.uket.domain.payment

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "payment")
class Payment(
    id: Long,
    organizationId: Long,
    accountNo: String,
    depositLink: String
) : BaseTimeEntity() {

    @Id @GeneratedValue
    @Column(name = "payment_id")
    var id: Long = id

    @Column(nullable = false)
    var organizationId: Long = organizationId
        protected set

    @Column(nullable = false)
    var accountNo: String = accountNo
        protected set

    @Column(nullable = false)
    var depositLink: String = depositLink
        protected set
}
