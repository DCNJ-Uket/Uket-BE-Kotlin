package uket.uket.domain.reservation

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "ticket")
class Ticket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val usersId: Long,
    val entryGroupId: Long,
    @Enumerated(EnumType.STRING)
    val status: TicketStatus,
    val ticketNo: String,
) : BaseTimeEntity()
