package uket.infra.output.persistence.entity.ticket

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "ticket_id"))
class TicketEntity(
    var userId: Long = 0L,
    var entryGroupId: Long = 0L,
    var status: TicketStatus? = null,
    var ticketNo: String = ""
) : BaseTimeEntity() {
}
