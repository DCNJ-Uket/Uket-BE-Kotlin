package uket.api.admin.response

import uket.domain.admin.entity.Admin

data class GetAdminResponse(
    val id: Long,
    val organizationId: Long,
    val name: String,
    val email: String,
    var password: String?,
    val isSuperAdmin: Boolean,
) {
    companion object {
        fun from(admin: Admin): GetAdminResponse = GetAdminResponse(
            id = admin.id,
            organizationId = admin.organization.id,
            name = admin.name,
            email = admin.email,
            password = admin.password,
            isSuperAdmin = admin.isSuperAdmin,
        )
    }
}
