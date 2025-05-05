package uket.uket.modules.push.nhn

import org.springframework.stereotype.Component
import uket.uket.modules.client.NHNMessageClient
import uket.uket.modules.client.NHNMessageSendRequest
import uket.uket.modules.push.UserMessageSender

@Component
class NHNUserMessageSender(
    val nhnMessageClient: NHNMessageClient,
    val nhnMessageProperties: NHNMessageProperties,
) : UserMessageSender {
    override fun send(templateCode: String, receiver: UserMessageSender.Receiver) {
        sendToNHN(
            templateCode = templateCode,
            recipientList = listOf(
                NHNMessageSendRequest.Recipient(
                    recipientNo = receiver.receiverKey, templateParameter = receiver.context
                )
            )
        )
    }

    /**
     * 최대 1000명까지 동시 발송 가능
     */
    override fun sendBulk(templateCode: String, receivers: List<UserMessageSender.Receiver>) {
        sendToNHN(
            templateCode = templateCode,
            recipientList = receivers.map { receiver ->
                NHNMessageSendRequest.Recipient(
                    recipientNo = receiver.receiverKey, templateParameter = receiver.context
                )
            }
        )
    }

    private fun sendToNHN(templateCode: String, recipientList: List<NHNMessageSendRequest.Recipient>) {
        nhnMessageClient.send(
            appKey = nhnMessageProperties.appKey,
            secretKey = nhnMessageProperties.secretKey,
            request = NHNMessageSendRequest(
                senderKey = nhnMessageProperties.senderKey,
                templateCode = templateCode,
                recipientList = recipientList
            )
        )
    }
}
