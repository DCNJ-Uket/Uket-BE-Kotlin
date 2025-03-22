package uket.uket.domain.organization.dto

import uket.uket.domain.organization.entity.Organization

data class CreateAdminCommand(
    val organization: Organization,
    val name: String,
    val email: String,
    val password: String,
)
