package uket.uket.domain.payment.entity

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "payment")
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val organizationId: Long,
    val accountNo: String,
    val depositLink: String,
) : BaseTimeEntity()
