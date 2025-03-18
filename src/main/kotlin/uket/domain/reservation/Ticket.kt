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
    id: Long,
    usersId: Long,
    entryGroupId: Long,
    status: TicketStatus,
    ticketNo: String,
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "ticket_id")
    var id: Long = id

    @Column(nullable = false)
    var userId: Long = usersId
        protected set

    @Column(nullable = false)
    var entryGroupId: Long = entryGroupId
        protected set

    @Column(nullable = false)
    var status: TicketStatus = status
        protected set

    @Column(nullable = false)
    var ticketNo: String = ticketNo
        protected set
}
