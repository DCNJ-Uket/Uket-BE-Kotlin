package uket.auth.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
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
            .excludePathPatterns("/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs", "/error")
            .excludePathPatterns("/admin/users/login")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/favicon.ico")
            .addResourceLocations("classpath:/static/favicon.ico")
            .setCachePeriod(3600) // 1시간 캐시
    }
}
