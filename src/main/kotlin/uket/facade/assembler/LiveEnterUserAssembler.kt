package uket.facade.assembler

import org.springframework.stereotype.Component
import uket.api.admin.response.LiveEnterUserResponse
import uket.domain.reservation.entity.Ticket
import uket.domain.uketevent.entity.EntryGroup
import uket.domain.user.entity.User
import java.time.ZoneId

@Component
class LiveEnterUserAssembler {
    private val zoneId = ZoneId.of("Asia/Seoul")

    fun assemble(
        ticket: Ticket,
        user: User,
        entryGroup: EntryGroup,
    ): LiveEnterUserResponse {
        return LiveEnterUserResponse(
            enterTime = ticket.enterAt!!.atZone(zoneId),
            name = user.name,
            ticketDate = entryGroup.entryStartDateTime.atZone(zoneId),
            phoneNumber = user.phoneNumber!!,
            ticketStatus = ticket.status
        )
    }
}
