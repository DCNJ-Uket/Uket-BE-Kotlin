package uket.facade.message

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import uket.common.enums.EventType
import uket.uket.modules.push.ReceiverType
import uket.uket.modules.push.UserMessageSendEvent
import uket.uket.modules.push.UserMessageTemplate

@Component
class TicketCancel2MessageSendService(
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun send(
        userName: String,
        eventType: EventType,
        eventName: String,
        예매번호: String,
        userPhoneNumber: String,
    ) {
        applicationEventPublisher.publishEvent(
            UserMessageSendEvent(
                templateCode = UserMessageTemplate.티켓취소알림톡_입금대기중.code,
                command = UserMessageTemplate.티켓취소알림톡_입금대기중.티켓취소알림_입금대기중Command(
                    userName = userName,
                    eventType = eventType.krName,
                    eventName = eventName,
                    예매번호 = 예매번호
                ),
                receiverType = ReceiverType.PHONE_NUMBER,
                receiverKey = userPhoneNumber,
            )
        )
    }

}
