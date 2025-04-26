package uket.common.response

import org.springframework.data.domain.Page

data class CustomPageResponse<T>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val first: Boolean,
    val last: Boolean,
    val totalElements: Long,
    val totalPages: Int,
    val empty: Boolean,
) {
    constructor(page: Page<T>) : this(
        content = page.content,
        pageNumber = page.number + 1,
        pageSize = page.size,
        first = page.isFirst,
        last = page.isLast,
        totalElements = page.totalElements,
        totalPages = page.totalPages,
        empty = page.isEmpty
    )
}
