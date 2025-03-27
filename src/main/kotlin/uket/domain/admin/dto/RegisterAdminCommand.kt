package uket.domain.organization.dto

import uket.domain.organization.entity.Organization

data class RegisterAdminCommand(
    val organization: Organization,
    val name: String,
    val email: String,
    val password: String,
)
