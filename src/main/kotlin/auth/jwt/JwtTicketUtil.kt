package uket.auth.jwt

import io.jsonwebtoken.Jwts
import uket.auth.jwt.JwtValues.JWT_PAYLOAD_KEY_CATEGORY
import uket.auth.jwt.JwtValues.JWT_PAYLOAD_KEY_ID
import uket.auth.jwt.JwtValues.JWT_PAYLOAD_VALUE_TICKET
import java.util.Date
import javax.crypto.SecretKey

class JwtTicketUtil(
    private val tokenProperties: TokenProperties,
    private val secretKey: SecretKey,
) {
    fun getTicketId(token: String?): Long {
        val id = Jwts
            .parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload[JwtValues.JWT_PAYLOAD_KEY_ID] as Int
        return id.toLong()
    }

    fun createTicketToken(ticketId: Long?): String {
        val now = System.currentTimeMillis()

        return Jwts.builder()
            .claim(JWT_PAYLOAD_KEY_CATEGORY, JWT_PAYLOAD_VALUE_TICKET)
            .claim(JWT_PAYLOAD_KEY_ID, ticketId)
            .issuedAt(Date(now))
            .expiration(getTicketInfoExpiration(now))
            .signWith(secretKey)
            .compact()
    }

    private fun getTicketInfoExpiration(now: Long): Date {
        return Date(now + tokenProperties.expiration.ticketTokenExpiration)
    }
}
