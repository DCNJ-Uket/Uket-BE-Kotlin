package uket.uket.domain.user.dto

data class RegisterUserDto(
    val userId: Long,
    val depositorName: String,
    val phoneNumber: String,
)
