package uket.uket.api.admin

import uket.domain.admin.entity.Admin

data class GetAllUsersResponse(
    val users: List<Admin>,
)
