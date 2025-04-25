package uket.api.admin.request

import org.springframework.format.annotation.DateTimeFormat
import uket.domain.reservation.enums.TicketStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class SearchRequest(
    val status: TicketStatus? = null,
    val userName: String? = null,
    val phoneNumberLastFourDigits: String? = null,
    @DateTimeFormat(pattern = "yy.MM.dd")
    val showDate: LocalDate? = null,
    @DateTimeFormat(pattern = "yy.MM.dd HH:mm")
    val createdAt: LocalDateTime? = null,
    @DateTimeFormat(pattern = "yy.MM.dd HH:mm")
    val modifiedAt: LocalDateTime? = null,
)
