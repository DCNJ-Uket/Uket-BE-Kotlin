package uket.auth.config

import feign.Logger
import org.springframework.context.annotation.Bean

class FeignConfiguration {
    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }
}
