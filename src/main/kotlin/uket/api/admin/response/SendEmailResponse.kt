package uket.api.admin.response

import uket.domain.admin.entity.Admin

data class SendEmailResponse(
    val success: Boolean,
    val email: String,
) {
    companion object {
        fun from(admin: Admin): SendEmailResponse = SendEmailResponse(
            success = true,
            email = admin.email,
        )
    }
}
