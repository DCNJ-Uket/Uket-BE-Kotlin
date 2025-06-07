package uket.uket.modules.push

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class UserMessageSendEventListener(
    val userMessageSender: UserMessageSender,
    val getUserPhoneNumber: GetUserPhoneNumber,
) {
    @Async
    @EventListener
    fun send(event: UserMessageSendEvent) {
        with(event) {
            val template = getTemplateByCode(templateCode)

            userMessageSender.send(
                templateCode = template.code,
                referrer = template.referrer,
                receiver = UserMessageSender.Receiver(
                    receiverKey = getPhoneNumber(receiverType, receiverKey),
                    context = template.makeContext(command)
                )
            )
        }
    }

    @Async
    @EventListener
    fun sendBulk(event: UserMessageBulkSendEvent) {
        with(event) {
            val template = getTemplateByCode(templateCode)

            userMessageSender.sendBulk(
                templateCode = template.code,
                referrer = template.referrer,
                receivers = receivers.map {
                    UserMessageSender.Receiver(
                        receiverKey = getPhoneNumber(it.receiverType, it.receiverKey),
                        context = template.makeContext(command)
                    )
                }
            )
        }
    }

    private fun getPhoneNumber(
        receiverType: ReceiverType,
        receiverKey: String,
    ) = when (receiverType) {
        ReceiverType.USER_ID -> {
            val userId = receiverKey.toLong()
            getUserPhoneNumber.invoke(userId)
        }
        ReceiverType.PHONE_NUMBER -> receiverKey
    }

    private fun getTemplateByCode(code: String): UserMessageTemplate {
        return UserMessageTemplate::class.sealedSubclasses
            .mapNotNull { it.objectInstance }
            .first { it.code == code }
    }
}

data class UserMessageSendEvent(
    val templateCode: String,
    val command: UserMessageTemplate.Command,
    val receiverType: ReceiverType,
    val receiverKey: String,
)

data class UserMessageBulkSendEvent(
    val templateCode: String,
    val command: UserMessageTemplate.Command,
    val receivers: List<Receiver>,
) {
    data class Receiver(
        val receiverType: ReceiverType,
        val receiverKey: String,
    )
}
