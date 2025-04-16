package uket.api.user.response

import uket.domain.uketevent.dto.EventListItem

data class ActiveEventsResponse(
    private val events: List<EventListItem>,
)
