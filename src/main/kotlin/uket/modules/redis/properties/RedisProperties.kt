package uket.uket.modules.redis.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "spring.data.redis")
data class RedisProperties(
    val host: String,
    val port: Int
)
