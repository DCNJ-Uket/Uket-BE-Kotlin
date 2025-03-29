package uket.uket.api.admin.response

import uket.uket.api.admin.dto.AdminDto

data class GetAllUsersResponse(
    val users: List<AdminDto>,
)
