package uket.auth.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import uket.auth.interceptor.LoginInterceptor

@Configuration
class AuthConfig(
    private val loginInterceptor: LoginInterceptor,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loginInterceptor)
            .excludePathPatterns("/api/v1/dev/**")
            .excludePathPatterns("/api/v1/email/**")
            .excludePathPatterns("/api/v1/auth/**")
            .excludePathPatterns("/api/v1/users/register")
            .excludePathPatterns("/admin/**")
            .excludePathPatterns("/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs", "/error")
    }
}
