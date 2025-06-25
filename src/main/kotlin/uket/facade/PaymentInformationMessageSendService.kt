package uket.facade

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import uket.common.enums.BankCode
import uket.uket.modules.push.ReceiverType
import uket.uket.modules.push.UserMessageSendEvent
import uket.uket.modules.push.UserMessageTemplate

@Service
class PaymentInformationMessageSendService(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun send(
        eventName: String,
        ticketPrice: Int,
        bankCode: BankCode,
        accountNumber: String,
        depositorName: String,
        userName: String,
        userPhoneNumber: String,
    ) {
        applicationEventPublisher
            .publishEvent(
                UserMessageSendEvent(
                    templateCode = UserMessageTemplate.결제안내알림톡.code,
                    command = UserMessageTemplate.결제안내알림톡.결제안내Command(
                        eventName = eventName,
                        ticketPrice = ticketPrice.toString(),
                        bankName = bankCode.name,
                        accountNumber = accountNumber,
                        depositorName = depositorName,
                        userName = userName
                    ),
                    receiverType = ReceiverType.PHONE_NUMBER,
                    receiverKey = userPhoneNumber
                )
            )
    }
}
