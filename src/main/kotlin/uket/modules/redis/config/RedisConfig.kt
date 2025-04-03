package uket.uket.modules.redis.config

import org.springframework.context.annotation.Configuration

@Configuration
@EnableRedisRepositories
class RedisConfig(
    private val redisProperties: RedisProperties
) {
    companion object {
        private const val REDISSON_HOST_PREFIX = "redis://"
    }

    // Redis Auth
    fun getRedisProperties(): RedisProperties {
        return redisProperties
    }

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val config = RedisStandaloneConfiguration(redisProperties.host(), redisProperties.port())
        return LettuceConnectionFactory(config)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(redisConnectionFactory())

        val stringSerializer = StringRedisSerializer()
        redisTemplate.keySerializer = stringSerializer
        redisTemplate.valueSerializer = stringSerializer
        redisTemplate.hashKeySerializer = stringSerializer
        redisTemplate.hashValueSerializer = stringSerializer
        redisTemplate.setDefaultSerializer(stringSerializer)

        return redisTemplate
    }

    @Bean
    fun valueOperations(redisTemplate: RedisTemplate<String, String>): ValueOperations<String, String> {
        return redisTemplate.opsForValue()
    }

    // Redis Lock
    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        val redisAddress = "$REDISSON_HOST_PREFIX${redisProperties.host()}:${redisProperties.port()}"
        config.useSingleServer().setAddress(redisAddress)
        return Redisson.create(config)
    }
}
