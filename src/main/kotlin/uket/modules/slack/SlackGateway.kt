package uket.modules.slack

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.net.URI

@Component
class SlackGateway(
    private val objectMapper: ObjectMapper,
    @Value("\${app.slack.webhook.url}")
    private val url: String,
) {
    fun sendErrorMessage(dto: SlackBotDto) {
        val body = objectMapper.writeValueAsString(dto)
        val requestEntity = RequestEntity
            .post(URI.create(url))
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)

        RestTemplate().exchange(requestEntity, Void::class.java)
    }

    data class SlackBotDto(
        @JsonProperty("attachments")
        val attachments: List<SlackBotAttachmentDto>,
    )

    data class SlackBotAttachmentDto(
        @JsonProperty("color")
        val color: String,
        @JsonProperty("author_name")
        val authorName: String,
        @JsonProperty("title")
        val title: String,
        @JsonProperty("text")
        val text: String,
        @JsonProperty("fields")
        val fields: List<SlackBotFieldDto>,
    )

    data class SlackBotFieldDto(
        @JsonProperty("title")
        val title: String,
        @JsonProperty("value")
        val value: String,
        @JsonProperty("short")
        val shortField: Boolean,
    )
}
