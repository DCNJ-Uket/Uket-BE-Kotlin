package uket.api.admin.response

import uket.api.admin.dto.LiveEnterUserDto
import uket.common.aop.masking.Mask
import uket.common.aop.masking.MaskingType
import uket.domain.reservation.enums.TicketStatus
import java.time.ZoneId
import java.time.ZonedDateTime

class LiveEnterUserResponse(
    val enterTime: ZonedDateTime,
    val name: String,
    val ticketDate: ZonedDateTime,
    @Mask(type = MaskingType.PHONE)
    val phoneNumber: String,
    val ticketStatus: TicketStatus,
) {

}
