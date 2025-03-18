package uket.uket.domain.reservation

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "ticket")
class Ticket(
    _id: Long,
    val usersId: Long,
    val entryGroupId: Long,
    val status: TicketStatus,
    val ticketNo: String,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "ticket_id")
    val id: Long = _id
}
