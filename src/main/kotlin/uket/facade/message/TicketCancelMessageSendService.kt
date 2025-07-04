package uket.facade.message

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import uket.common.enums.EventType
import uket.uket.modules.push.ReceiverType
import uket.uket.modules.push.UserMessageSendEvent
import uket.uket.modules.push.UserMessageTemplate

@Service
class TicketCancelMessageSendService(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun send(
        userName: String,
        userPhoneNumber: String,
        eventName: String,
        eventType: EventType,
        ticketNo: String,
        organizationName: String,
    ) {
        applicationEventPublisher
            .publishEvent(
                UserMessageSendEvent(
                    templateCode = UserMessageTemplate.티켓취소알림톡.code,
                    command = UserMessageTemplate.티켓취소알림톡.티켓취소알림Command(
                        userName = userName,
                        eventName = eventName,
                        eventType = eventType.krName,
                        organizationName = organizationName,
                        예매번호 = ticketNo,
                    ),
                    receiverType = ReceiverType.PHONE_NUMBER,
                    receiverKey = userPhoneNumber
                )
            )
    }
}
