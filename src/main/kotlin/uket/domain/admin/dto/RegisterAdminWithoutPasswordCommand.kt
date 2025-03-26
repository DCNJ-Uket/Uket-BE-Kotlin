package uket.uket.domain.organization.dto

import uket.uket.domain.organization.entity.Organization

data class RegisterAdminWithoutPasswordCommand(
    val organization: Organization,
    val name: String,
    val email: String,
)
