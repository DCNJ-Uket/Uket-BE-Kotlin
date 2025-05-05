package uket.uket.api.user.response

data class TicketingResponse(
    val ticketIds: List<Long>,
    val totalPrice: Long,
    val depositUrl: String,
    val bankCode: String,
)
