package uket.api.user.response

import uket.domain.uketevent.dto.EventListItem

data class ActiveEventsResponse(
    val events: List<EventListItem>,
)
