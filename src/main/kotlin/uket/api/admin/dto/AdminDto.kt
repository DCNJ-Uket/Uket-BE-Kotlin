package uket.uket.api.admin.dto

import uket.domain.admin.entity.Admin

data class AdminDto(
    val id: Long,
    val organizationId: Long,
    val name: String,
    val email: String,
    var password: String?,
    val isSuperAdmin: Boolean,
) {
    companion object {
        fun from(admin: Admin): AdminDto = AdminDto(
            admin.id,
            admin.organization.id,
            admin.name,
            admin.email,
            admin.password,
            admin.isSuperAdmin,
        )
    }
}
