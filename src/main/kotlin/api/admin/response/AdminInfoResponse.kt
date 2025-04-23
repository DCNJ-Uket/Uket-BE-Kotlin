package uket.api.admin.response

import uket.domain.admin.dto.AdminWithOrganizationDto

data class AdminInfoResponse(
    val adminId: Long,
    val organization: String,
    val name: String,
    val email: String,
    val isSuperAdmin: Boolean,
) {
    companion object {
        fun from(adminInfo: AdminWithOrganizationDto): AdminInfoResponse {
            return AdminInfoResponse(
                adminId = adminInfo.id,
                organization = adminInfo.organizationName,
                name = adminInfo.name,
                email = adminInfo.email,
                isSuperAdmin = adminInfo.isSuperAdmin,
            )
        }
    }
}
