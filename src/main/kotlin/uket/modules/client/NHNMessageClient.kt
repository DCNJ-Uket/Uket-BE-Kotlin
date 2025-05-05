package uket.uket.modules.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import uket.auth.config.FeignConfiguration

@FeignClient(
    value = "nhn-message",
    url = "https://api-alimtalk.cloud.toast.com",
    configuration = [FeignConfiguration::class]
)
interface NHNMessageClient {
    @PostMapping("/alimtalk/v2.3/appkeys/{appKey}/messages")
    fun send(
        @PathVariable("appKey") appKey: String,
        @RequestHeader("X-Secret-Key") secretKey: String,
        @RequestBody request: NHNMessageSendRequest,
    )
}

data class NHNMessageSendRequest(
    val senderKey: String,
    val templateCode: String,
    val recipientList: List<Recipient>,
) {
    data class Recipient(
        val recipientNo: String,
        val templateParameter: Map<String, String>,
    )
}
