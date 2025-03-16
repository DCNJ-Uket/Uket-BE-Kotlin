package uket.infra.output.persistence.entity.ticket

import jakarta.persistence.*
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
@AttributeOverride(name = "id", column = Column(name = "ticket_id"))
class TicketEntity(
    userId: Long,
    entryGroupId: Long,
    status: TicketStatus?,
    ticketNo: String
) : BaseTimeEntity() {
    var userId: Long = userId
        protected set

    var entryGroupId: Long = entryGroupId
        protected set

    var status: TicketStatus? = status
        protected set

    var ticketNo: String = ticketNo
        protected set
}
