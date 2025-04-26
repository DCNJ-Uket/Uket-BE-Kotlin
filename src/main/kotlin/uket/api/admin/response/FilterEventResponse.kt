package uket.api.admin.response

import uket.api.admin.dto.EventNameDto

data class FilterEventResponse(
    val items: List<EventNameDto>,
)
