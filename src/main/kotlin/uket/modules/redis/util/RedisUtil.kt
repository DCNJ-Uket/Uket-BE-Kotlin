package uket.uket.modules.redis.util

import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class RedisUtil(
    private val valueOperations: ValueOperations<String, String>
) {

    fun setDataExpire(key: String, value: String, duration: Long) {
        val expireDuration = Duration.ofMillis(duration)
        valueOperations.set(key, value, expireDuration)
    }

    fun getData(key: String): Optional<String> {
        val value = valueOperations.get(key)
        return Optional.ofNullable(value)
    }
}
