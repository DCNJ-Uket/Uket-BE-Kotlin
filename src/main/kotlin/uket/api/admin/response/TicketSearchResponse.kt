package uket.api.admin.response

import org.springframework.data.domain.Page
import uket.common.aop.masking.Mask
import uket.common.aop.masking.MaskingType
import uket.domain.reservation.dto.TicketSearchDto
import java.time.ZoneId
import java.time.ZonedDateTime

data class TicketSearchResponse(
    val ticketId: Long,
    val depositorName: String,
    @Mask(type = MaskingType.PHONE)
    val telephone: String,
    val showTime: ZonedDateTime,
    val orderDate: ZonedDateTime,
    val updatedDate: ZonedDateTime,
    val ticketStatus: String,
    val performer: String,
) {
    companion object {
        private val zoneId = ZoneId.of("Asia/Seoul")

        fun from(ticket: TicketSearchDto): TicketSearchResponse = TicketSearchResponse(
            ticketId = ticket.ticketId,
            depositorName = ticket.depositorName,
            telephone = ticket.telephone,
            showTime = ticket.showTime.atZone(zoneId),
            orderDate = ticket.orderDate.atZone(zoneId),
            updatedDate = ticket.updatedDate.atZone(zoneId),
            ticketStatus = ticket.ticketStatus.value,
            performer = ticket.performer
        )

        fun from(tickets: Page<TicketSearchDto>): Page<TicketSearchResponse> = tickets.map { from(it) }
    }
}
