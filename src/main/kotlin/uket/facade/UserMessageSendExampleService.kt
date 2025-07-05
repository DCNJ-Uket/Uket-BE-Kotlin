package uket.uket.facade

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import uket.common.enums.EventType
import uket.uket.modules.push.ReceiverType
import uket.uket.modules.push.UserMessageBulkSendEvent
import uket.uket.modules.push.UserMessageSendEvent
import uket.uket.modules.push.UserMessageTemplate

@Deprecated("알림톡 발송 사용 예시를 위한 서비스로 삭제 필요")
@Service
class UserMessageSendExampleService(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun send() {
        applicationEventPublisher.publishEvent(
            UserMessageSendEvent(
                templateCode = UserMessageTemplate.티켓취소알림톡.code,
                command = UserMessageTemplate.티켓취소알림톡.티켓취소알림Command(
                    userName = "홍길동",
                    eventName = "소리터",
                    organizationName = "소리터",
                    eventType = EventType.FESTIVAL.krName,
                    예매번호 = "123"
                ),
                receiverType = ReceiverType.PHONE_NUMBER,
                receiverKey = "01053316762"
            )
        )
    }

    fun sendBulk() {
        applicationEventPublisher.publishEvent(
            UserMessageBulkSendEvent(
                templateCode = UserMessageTemplate.티켓취소알림톡.code,
                command = UserMessageTemplate.티켓취소알림톡.티켓취소알림Command(
                    userName = "홍길동",
                    eventName = "소리터",
                    organizationName = "소리터",
                    eventType = EventType.FESTIVAL.krName,
                    예매번호 = "123"
                ),
                receivers = listOf(
                    UserMessageBulkSendEvent.Receiver(
                        receiverType = ReceiverType.USER_ID,
                        receiverKey = "1"
                    ),
                    UserMessageBulkSendEvent.Receiver(
                        receiverType = ReceiverType.PHONE_NUMBER,
                        receiverKey = "01012345678"
                    )
                ),
            )
        )
    }
}
