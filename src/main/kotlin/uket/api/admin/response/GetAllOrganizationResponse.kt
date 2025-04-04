package uket.uket.api.admin.response

import uket.domain.admin.entity.Organization

data class GetAllOrganizationResponse(
    val organizations: List<Organization>,
)
