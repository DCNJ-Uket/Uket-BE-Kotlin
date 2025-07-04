package uket.facade.message

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import uket.domain.payment.entity.Payment
import uket.uket.modules.push.ReceiverType
import uket.uket.modules.push.UserMessageSendEvent
import uket.uket.modules.push.UserMessageTemplate

@Service
class PaymentInformationMessageSendService(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    fun send(
        eventName: String,
        ticketPrice: Long,
        account: Payment.Account,
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
                        bankName = account.bankCode.name,
                        accountNumber = account.accountNumber,
                        depositorName = account.depositorName,
                        userName = userName
                    ),
                    receiverType = ReceiverType.PHONE_NUMBER,
                    receiverKey = userPhoneNumber
                )
            )
    }
}
