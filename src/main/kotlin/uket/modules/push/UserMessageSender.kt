package uket.uket.modules.push

interface UserMessageSender {
    fun send(
        templateCode: String,
        referrer: String,
        receiver: Receiver,
    )

    fun sendBulk(
        templateCode: String,
        referrer: String,
        receivers: List<Receiver>,
    )

    data class Receiver(
        val receiverKey: String,
        val context: Map<String, String>,
    )
}
