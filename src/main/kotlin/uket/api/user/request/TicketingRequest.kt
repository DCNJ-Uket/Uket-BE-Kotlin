package uket.uket.api.user.request

data class TicketingRequest(
    val entryGroupId: Long,
    val ticketCount: Int,
    val friend: String,
)
