package uket.infra.output.persistence.entity.ticket

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
class TicketEntity(
    var userId: Long = 0L,
    var entryGroupId: Long = 0L,
    var status: TicketStatus? = null,
    var ticketNo: String = ""
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "ticket_id")
    private var id: Long = 0L
}
