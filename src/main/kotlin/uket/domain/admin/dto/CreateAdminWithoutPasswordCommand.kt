package uket.uket.domain.organization.dto

import uket.uket.domain.organization.entity.Organization

data class CreateAdminWithoutPasswordCommand(
    val organization: Organization,
    val name: String,
    val email: String,
)
