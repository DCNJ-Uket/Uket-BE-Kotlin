package uket.uket.api.admin.response

import uket.domain.admin.entity.Admin

data class GetAllUsersResponse(
    val users: List<Admin>,
)
