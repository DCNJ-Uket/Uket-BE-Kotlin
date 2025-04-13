package uket.api.admin.response

import uket.domain.admin.entity.Admin
import uket.domain.admin.entity.Organization

data class RegisterAdminResponse(
    val adminId: Long,
    val organization: String,
    val name: String,
    val email: String,
    var password: String?,
    val isSuperAdmin: Boolean,
) {
    companion object {
        fun of(admin: Admin, organization: Organization): RegisterAdminResponse {
            return RegisterAdminResponse(
                adminId = admin.id,
                organization = organization.name,
                name = admin.name,
                email = admin.email,
                password = admin.password,
                isSuperAdmin = admin.isSuperAdmin,
            )
        }
    }
}
