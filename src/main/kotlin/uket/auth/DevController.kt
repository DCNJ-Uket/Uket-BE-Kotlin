package uket.uket.auth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import uket.uket.auth.jwt.JwtAuthTokenUtil

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
}
