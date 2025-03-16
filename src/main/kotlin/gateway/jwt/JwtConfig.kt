package uket.gateway.jwt

import io.jsonwebtoken.Jwts
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Configuration
class JwtConfig(
    private val tokenProperties: TokenProperties,
) {
    private var secretKey: SecretKey = SecretKeySpec(
        tokenProperties.secretKey.toByteArray(StandardCharsets.UTF_8),
        Jwts.SIG.HS256.key().build().algorithm,
    )

    @Bean
    fun jwtAuthTokenUtil(): JwtAuthTokenUtil {
        return JwtAuthTokenUtil(tokenProperties, secretKey)
    }
}
