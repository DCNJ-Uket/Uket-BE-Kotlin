package uket.uket.domain.reservation.entity

import jakarta.persistence.*
import uket.uket.domain.DeletableEntity
import uket.uket.domain.reservation.enums.TicketStatus
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
    fun cancel() {
        this.status = TicketStatus.RESERVATION_CANCEL
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
