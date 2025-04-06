package uket.auth.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import uket.uket.domain.user.enums.UserRole
import java.security.SignatureException
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

class JwtAuthTokenUtil(
    private val tokenProperties: TokenProperties,
    private val secretKey: SecretKey,
) {
    fun getCategory(token: String?): String = Jwts
        .parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload[JwtValues.JWT_PAYLOAD_KEY_CATEGORY] as String

    fun getId(token: String?): Long {
        val id = Jwts
            .parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload[JwtValues.JWT_PAYLOAD_KEY_ID] as Int
        return id.toLong()
    }

    fun getName(token: String?): String = Jwts
        .parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload[JwtValues.JWT_PAYLOAD_KEY_NAME] as String

    fun getRole(token: String?): String = Jwts
        .parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload[JwtValues.JWT_PAYLOAD_KEY_ROLE] as String

    fun getEmail(token: String?): String = Jwts
        .parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload[JwtValues.JWT_PAYLOAD_KEY_EMAIL] as String

    fun isRegistered(token: String?): Boolean = Jwts
        .parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload[JwtValues.JWT_PAYLOAD_KEY_REGISTERED] as Boolean

    fun isExpired(token: String?): Boolean {
        try {
            Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
                .expiration
        } catch (e: ExpiredJwtException) {
            return true
        }
        return false
    }

    fun isValidToken(token: String?): Boolean {
        try {
            Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
        } catch (e: SignatureException) {
            return false
        }
        return true
    }

    fun createAccessToken(userId: Long?, name: String?, role: String?, isRegistered: Boolean?): String {
        val now = System.currentTimeMillis()

        return Jwts
            .builder()
            .claim(JwtValues.JWT_PAYLOAD_KEY_CATEGORY, JwtValues.JWT_PAYLOAD_VALUE_ACCESS)
            .claim(JwtValues.JWT_PAYLOAD_KEY_ID, userId)
            .claim(JwtValues.JWT_PAYLOAD_KEY_NAME, name)
            .claim(JwtValues.JWT_PAYLOAD_KEY_ROLE, role)
            .claim(JwtValues.JWT_PAYLOAD_KEY_REGISTERED, isRegistered)
            .issuedAt(Date(now))
            .expiration(getAccessTokenExpiration(now))
            .signWith(secretKey)
            .compact()
    }

    fun createEmailToken(adminId: Long?, email: String?, name: String?, isRegistered: Boolean?): String {
        val now = System.currentTimeMillis()

        return Jwts
            .builder()
            .claim(JwtValues.JWT_PAYLOAD_KEY_CATEGORY, JwtValues.JWT_PAYLOAD_VALUE_ACCESS)
            .claim(JwtValues.JWT_PAYLOAD_KEY_ID, adminId)
            .claim(JwtValues.JWT_PAYLOAD_KEY_NAME, name)
            .claim(JwtValues.JWT_PAYLOAD_KEY_EMAIL, email)
            .claim(JwtValues.JWT_PAYLOAD_KEY_ROLE, UserRole.ADMIN)
            .claim(JwtValues.JWT_PAYLOAD_KEY_REGISTERED, isRegistered)
            .issuedAt(Date(now))
            .expiration(getEmailTokenExpiration(now))
            .signWith(secretKey)
            .compact()
    }

    fun createAccessToken(userId: Long?, name: String?, role: String?, isRegistered: Boolean?, expiration: Long?): String {
        val now = System.currentTimeMillis()

        return Jwts
            .builder()
            .claim(JwtValues.JWT_PAYLOAD_KEY_CATEGORY, JwtValues.JWT_PAYLOAD_VALUE_ACCESS)
            .claim(JwtValues.JWT_PAYLOAD_KEY_ID, userId)
            .claim(JwtValues.JWT_PAYLOAD_KEY_NAME, name)
            .claim(JwtValues.JWT_PAYLOAD_KEY_ROLE, role)
            .claim(JwtValues.JWT_PAYLOAD_KEY_REGISTERED, isRegistered)
            .issuedAt(Date(now))
            .expiration(Date(expiration!!))
            .signWith(secretKey)
            .compact()
    }

    fun createRefreshToken(): String {
        val now = System.currentTimeMillis()
        val uuid = UUID.randomUUID()

        return Jwts
            .builder()
            .claim(JwtValues.JWT_PAYLOAD_KEY_CATEGORY, JwtValues.JWT_PAYLOAD_VALUE_REFRESH)
            .claim(JwtValues.JWT_PAYLOAD_KEY_UUID, uuid)
            .issuedAt(Date(now))
            .expiration(getRefreshTokenExpiration(now))
            .signWith(secretKey)
            .compact()
    }

    fun createRefreshToken(expiration: Long): String {
        val now = System.currentTimeMillis()
        val uuid = UUID.randomUUID()

        return Jwts
            .builder()
            .claim(JwtValues.JWT_PAYLOAD_KEY_CATEGORY, JwtValues.JWT_PAYLOAD_VALUE_REFRESH)
            .claim(JwtValues.JWT_PAYLOAD_KEY_UUID, uuid)
            .issuedAt(Date(now))
            .expiration(Date(now + expiration))
            .signWith(secretKey)
            .compact()
    }

    private fun getAccessTokenExpiration(now: Long): Date = Date(now + (tokenProperties.expiration.accessTokenExpiration))

    private fun getRefreshTokenExpiration(now: Long): Date = Date(now + (tokenProperties.expiration.refreshTokenExpiration))

    private fun getEmailTokenExpiration(now: Long): Date = Date(now + (tokenProperties.expiration.emailTokenExpiration))
}
