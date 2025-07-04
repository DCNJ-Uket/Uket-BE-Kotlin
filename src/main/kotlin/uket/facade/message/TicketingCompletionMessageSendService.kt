package uket.facade.message

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import uket.common.enums.EventType
import uket.uket.modules.push.ReceiverType
import uket.uket.modules.push.UserMessageSendEvent
import uket.uket.modules.push.UserMessageTemplate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Service
class TicketingCompletionMessageSendService(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun send(
        userName: String,
        userPhoneNumber: String,
        eventName: String,
        eventType: EventType,
        ticketNo: String,
        eventDate: LocalDateTime,
        eventLocation: String,
    ) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.KOREAN)
        val timeFormatter = DateTimeFormatter.ofPattern("HH시 mm분", Locale.KOREAN)
        val dateString = eventDate
            .format(dateFormatter)
        val timeString = eventDate
            .format(timeFormatter)

        applicationEventPublisher
            .publishEvent(
                UserMessageSendEvent(
                    templateCode = UserMessageTemplate.예매완료알림톡.code,
                    command = UserMessageTemplate.예매완료알림톡.예매완료알림Command(
                        userName = userName,
                        eventName = eventName,
                        eventType = eventType.krName,
                        ticketNo = ticketNo,
                        행사일자 = dateString,
                        행사시간 = timeString,
                        행사장소 = eventLocation
                    ),
                    receiverType = ReceiverType.PHONE_NUMBER,
                    receiverKey = userPhoneNumber
                )
            )
    }
}
