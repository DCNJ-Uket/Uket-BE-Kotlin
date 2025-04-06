package uket.uket.api.admin.response

import uket.domain.admin.entity.Admin
import uket.domain.admin.entity.Organization

data class RegisterAdminResponse(
    val adminId: Long,
    val organization: String,
    val name: String,
    val email: String,
    var password: String?,
    val authority: String,
) {
    companion object {
        fun of(admin: Admin, organization: Organization, authority: String): RegisterAdminResponse {
            return RegisterAdminResponse(
                adminId = admin.id,
                organization = organization.name,
                name = admin.name,
                email = admin.email,
                password = admin.password,
                authority = authority,
            )
        }
    }
}
