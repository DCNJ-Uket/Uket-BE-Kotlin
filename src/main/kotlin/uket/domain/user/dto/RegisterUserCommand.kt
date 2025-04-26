package uket.domain.user.dto

data class RegisterUserCommand(
    val userId: Long,
    val depositorName: String,
    val phoneNumber: String,
)
