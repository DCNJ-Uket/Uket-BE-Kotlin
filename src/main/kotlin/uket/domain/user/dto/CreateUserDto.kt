package uket.uket.domain.user.dto

import uket.uket.domain.user.enums.Platform

data class CreateUserDto(
    val platform: Platform,
    val platformId: String,
    val name: String,
    val email: String,
    val profileImage: String,
)
