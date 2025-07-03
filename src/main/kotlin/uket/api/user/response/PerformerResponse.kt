package uket.api.user.response

import uket.domain.uketevent.entity.Performer

data class PerformerResponse(
    val performerId: Long,
    val name: String,
    val ticketCount: Int,
) {
    companion object {
        fun from(performer: Performer): PerformerResponse = PerformerResponse(
            performer.id,
            performer.name,
            performer.totalTicketCount
        )
    }
}
