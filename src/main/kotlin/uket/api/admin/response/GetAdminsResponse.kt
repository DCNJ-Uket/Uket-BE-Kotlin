package uket.uket.api.admin.response

import uket.uket.api.admin.dto.AdminDto

data class GetAdminsResponse(
    val users: List<AdminDto>,
    val pageNumber: Int,
    val pageSize: Int,
    val isFirst: Boolean,
    val isLast: Boolean,
    val totalElements: Long,
    val totalPages: Int,
    val isEmpty: Boolean,
)
