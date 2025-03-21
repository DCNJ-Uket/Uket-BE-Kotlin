package uket.uket.domain.user.dto

import uket.uket.domain.user.entity.User

data class UserDeleteDto(
    val userId: Long,
    val name: String,
) {
    companion object {
        fun from(user: User): UserDeleteDto = UserDeleteDto(
            user.id,
            user.name,
        )
    }
}
