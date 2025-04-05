package uket.uket.modules.redis.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import uket.uket.modules.redis.util.RedisValues
import java.time.Duration

@Service
class RotateTokenService(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    fun storeToken(refreshToken: String, accessToken: String, userId: Long) {
        val refreshTokenKey = "refreshToken:$refreshToken"
        deleteTokenIfExist(refreshToken)

        val tokenDetails = mapOf(
            RedisValues.REDIS_KEY_ACCESS_TOKEN to accessToken,
            RedisValues.REDIS_KEY_USER_ID to userId.toString(),
        )

        redisTemplate.opsForHash<String, String>().putAll(refreshTokenKey, tokenDetails)
        redisTemplate.expire(refreshTokenKey, Duration.ofHours(2))
    }

    fun getUserIdForToken(refreshToken: String): Long {
        val refreshTokenKey = "refreshToken:$refreshToken"
        val userIdAsString = redisTemplate.opsForHash<String, String>()
            .get(refreshTokenKey, RedisValues.REDIS_KEY_USER_ID)

        return userIdAsString?.toLong()
            ?: throw IllegalStateException("유저 아이디를 찾을 수 없습니다.")
    }

    fun getAccessTokenForToken(refreshToken: String): String? {
        val refreshTokenKey = "refreshToken:$refreshToken"
        return redisTemplate.opsForHash<String, String>()
            .get(refreshTokenKey, RedisValues.REDIS_KEY_ACCESS_TOKEN)
    }

    fun validateRefreshToken(refreshToken: String) {
        val refreshTokenKey = "refreshToken:$refreshToken"
        if (redisTemplate.hasKey(refreshTokenKey) != true) {
            throw IllegalStateException("유효하지않는 토큰 입니다.")
        }
    }

    private fun deleteTokenIfExist(refreshToken: String) {
        val refreshTokenKey = "refreshToken:$refreshToken"
        redisTemplate.delete(refreshTokenKey)
    }
}
