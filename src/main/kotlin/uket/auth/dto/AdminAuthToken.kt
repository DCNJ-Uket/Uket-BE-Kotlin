package uket.auth.dto

import uket.domain.admin.entity.Admin

data class AdminAuthToken(
    val accessToken: String,
    val name: String,
    val email: String,
    val isSuperAdmin: Boolean,
    val organization: String,
) {
    companion object {
        fun of(accessToken: String, admin: Admin, organization: String): AdminAuthToken {
            return AdminAuthToken(accessToken, admin.name, admin.email, admin.isSuperAdmin, organization)
        }
    }
}
