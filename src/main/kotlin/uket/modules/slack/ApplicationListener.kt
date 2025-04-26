package uket.modules.slack

import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import uket.modules.slack.dto.ErrorReportDto

@Component
class ApplicationListener(
    private val slackGateway: SlackGateway,
    private val environment: Environment,
) {
    @Async
    @EventListener
    fun onErrorReport(event: ErrorReportDto) {
        if (environment.activeProfiles.contains("local")) return

        val errorMessage = event.errorMessage ?: "알 수 없는 에러"
        val dto = SlackGateway.SlackBotDto(
            attachments = listOf(
                SlackGateway.SlackBotAttachmentDto(
                    color = "#ff2400",
                    authorName = "uket-server",
                    title = "백엔드 서비스 에러 리포트",
                    text = "백엔드 서버에서 핸들링되지 않은 오류가 발생하였습니다",
                    fields = listOf(
                        SlackGateway.SlackBotFieldDto(
                            title = "에러 메시지",
                            value = errorMessage,
                            shortField = false
                        ),
                        SlackGateway.SlackBotFieldDto(
                            title = "에러 페이로드",
                            value = event.payload,
                            shortField = false
                        )
                    )
                )
            )
        )

        slackGateway.sendErrorMessage(dto)
    }
}
