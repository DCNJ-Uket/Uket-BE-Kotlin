package uket.common.response

data class ListResponse<T>(
    val items: List<T>,
)
