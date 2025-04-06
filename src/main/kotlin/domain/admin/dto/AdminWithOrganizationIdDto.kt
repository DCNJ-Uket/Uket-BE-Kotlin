package uket.domain.admin.dto

import uket.domain.admin.entity.Admin

data class AdminWithOrganizationIdDto(
    val id: Long,
    val organizationId: Long,
    val name: String,
    val email: String,
    var password: String?,
    val isSuperAdmin: Boolean,
) {
    companion object {
        fun from(admin: Admin): AdminWithOrganizationIdDto = AdminWithOrganizationIdDto(
            id = admin.id,
            organizationId = admin.organization.id,
            name = admin.name,
            email = admin.email,
            password = admin.password,
            isSuperAdmin = admin.isSuperAdmin,
        )
    }
}
