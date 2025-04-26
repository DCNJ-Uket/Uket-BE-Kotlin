package uket.auth.oauth

import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.RestClient
import uket.common.LoggerDelegate
import java.nio.charset.StandardCharsets

open class OAuth2Manager {
    private val log by LoggerDelegate()

    companion object {
        @JvmStatic
        protected val MEDIA_TYPE: String = MediaType(
            MediaType.APPLICATION_FORM_URLENCODED,
            StandardCharsets.UTF_8
        ).toString()
    }

    fun createRestClient(baseUrl: String): RestClient = RestClient
        .builder()
        .baseUrl(baseUrl)
        .defaultStatusHandler(
            { obj: HttpStatusCode -> obj.is4xxClientError },
            { request: HttpRequest, response: ClientHttpResponse ->
                log.error("Client Error Code={}", response.statusCode)
                log.error(
                    "Client Error Message={}",
                    String(response.body.readAllBytes())
                )
            }
        ).defaultStatusHandler(
            { obj: HttpStatusCode -> obj.is5xxServerError },
            { request: HttpRequest?, response: ClientHttpResponse ->
                log.error("Server Error Code={}", response.statusCode)
                log.error(
                    "Server Error Message={}",
                    String(response.body.readAllBytes())
                )
            }
        ).build()
}
