package uket.domain.user.dto

import uket.domain.user.enums.Platform

data class CreateUserCommand(
    val platform: Platform,
    val platformId: String,
    val name: String,
    val email: String,
    val profileImage: String,
)
