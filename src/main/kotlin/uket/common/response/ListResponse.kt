package uket.uket.common.response

class ListResponse<T>(
    val items: List<T>,
) {
    companion object {
        fun <T> from(items: List<T>): ListResponse<T> = ListResponse(items)
    }
}
