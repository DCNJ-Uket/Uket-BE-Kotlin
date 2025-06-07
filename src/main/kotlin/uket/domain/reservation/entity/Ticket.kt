package uket.domain.reservation.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.domain.DeletableEntity
import uket.domain.reservation.enums.TicketStatus
import java.time.LocalDateTime

@Entity
@Table(name = "ticket")
class Ticket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val userId: Long,
    val entryGroupId: Long,
    @Enumerated(EnumType.STRING)
    var status: TicketStatus,
    val ticketNo: String,
    var enterAt: LocalDateTime?,
) : DeletableEntity() {
    fun cancelBeforePayment() {
        this.status = TicketStatus.RESERVATION_CANCEL
    }

    fun cancelAfterPayment() {
        this.status = TicketStatus.REFUND_REQUESTED
    }

    fun enter() {
        this.status = TicketStatus.FINISH_ENTER
        this.enterAt = LocalDateTime.now()
    }

    fun updateStatus(ticketStatus: TicketStatus): Ticket {
        this.status = ticketStatus
        return this
    }
}
