package uket.domain.admin.dto

import uket.domain.admin.entity.Admin

data class AdminWithOrganizationDto(
    val id: Long,
    val organizationId: Long,
    val organizationName: String,
    val name: String,
    val email: String,
    var password: String?,
    val isSuperAdmin: Boolean,
) {
    companion object {
        fun from(admin: Admin): AdminWithOrganizationDto = AdminWithOrganizationDto(
            id = admin.id,
            organizationId = admin.organization.id,
            organizationName = admin.organization.name,
            name = admin.name,
            email = admin.email,
            password = admin.password,
            isSuperAdmin = admin.isSuperAdmin,
        )
    }
}
