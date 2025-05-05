package uket.uket.facade

import org.springframework.stereotype.Component
import uket.domain.user.service.UserService
import uket.uket.modules.push.GetUserPhoneNumber

@Component
class GetUserPhoneNumberImpl(
    val userService: UserService,
) : GetUserPhoneNumber {
    override fun invoke(userId: Long): String {
        return userService.getById(userId).phoneNumber
            ?: error("[GetUserPhoneNumberImpl] 유저에 해당하는 전화번호가 없습니다. 가입이 완료된 유저인지 확인이 필요합니다. | userId: $userId")
    }
}
