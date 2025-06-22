package uket.uket.facade

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import uket.common.enums.EventType
import uket.uket.modules.push.ReceiverType
import uket.uket.modules.push.UserMessageSendEvent
import uket.uket.modules.push.UserMessageTemplate

@Service
class TicketingCompletionMessageSendService(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun send(userName: String, userPhoneNumber: String, eventName: String, eventType: EventType, ticketNo: String, eventDate: String, eventLocation: String) {
        applicationEventPublisher.publishEvent(
            UserMessageSendEvent(
                templateCode = UserMessageTemplate.예매완료알림톡.code,
                command = UserMessageTemplate.예매완료알림톡.예매완료알림Command(
                    userName = userName,
                    eventName = eventName,
                    eventType = eventType.krName,
                    ticketNo = ticketNo,
                    행사일시 = eventDate,
                    행사장소 = eventLocation
                ),
                receiverType = ReceiverType.PHONE_NUMBER,
                receiverKey = userPhoneNumber
            )
        )
    }
}
