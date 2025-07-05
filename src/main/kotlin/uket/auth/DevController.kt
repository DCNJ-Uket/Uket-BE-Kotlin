package uket.auth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import uket.auth.jwt.JwtAuthTokenUtil

@RestController
class DevController(
    private val jwtAuthTokenUtil: JwtAuthTokenUtil,
) {
    @GetMapping("/api/v1/dev/token")
    fun getToken(): String {
        val newAccessToken = jwtAuthTokenUtil.createAccessToken(
            1,
            "홍길동",
            "USER",
            true,
        )
        return newAccessToken
    }

    @GetMapping("/timezone")
    fun checkTime(): Map<String, String> {
        // 1. JVM의 기본 시간대 정보 출력
        val jvmDefaultZone = java.util.TimeZone
            .getDefault()
            .id

        // 2. LocalDateTime.now()가 어떤 값을 생성하는지 확인
        val localDateTimeNow = java.time.LocalDateTime
            .now()
            .toString()

        // 3. ZonedDateTime.now()가 어떤 값을 생성하는지 확인
        val zonedDateTimeNow = java.time.ZonedDateTime
            .now()
            .toString()

        return mapOf(
            "jvm.default.timezone" to jvmDefaultZone,
            "LocalDateTime.now()" to localDateTimeNow,
            "ZonedDateTime.now()" to zonedDateTimeNow
        )
    }
}
