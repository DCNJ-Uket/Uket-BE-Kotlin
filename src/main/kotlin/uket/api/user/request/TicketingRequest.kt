package uket.api.user.request

data class TicketingRequest(
    val entryGroupId: Long,
    val buyCount: Int,
    val friend: String,
)
