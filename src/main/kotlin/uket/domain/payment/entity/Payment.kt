package uket.domain.payment.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.domain.BaseTimeEntity

@Entity
@Table(name = "payment")
class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "uket_event_id")
    val uketEventId: Long,

    @Column(name = "ticket_price")
    val ticketPrice: Long,
    @Column(name = "bank_code")
    val bankCode: String,
    @Column(name = "account_number")
    val accountNumber: String,
    @Column(name = "depositor_name")
    val depositorName: String,
    @Column(name = "deposit_url")
    val depositUrl: String,
) : BaseTimeEntity()
