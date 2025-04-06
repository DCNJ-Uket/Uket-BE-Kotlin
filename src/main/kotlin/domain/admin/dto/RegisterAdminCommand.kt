package uket.domain.admin.dto

import uket.domain.admin.entity.Organization

data class RegisterAdminCommand(
    val organization: Organization,
    val name: String,
    val email: String,
    val password: String,
)
