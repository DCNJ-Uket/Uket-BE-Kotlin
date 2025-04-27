package uket.auth.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import uket.auth.config.adminId.LoginAdminIdArgumentResolver
import uket.auth.config.userId.LoginUserIdArgumentResolver
import uket.auth.interceptor.LoginInterceptor

@Configuration
class AuthConfig(
    private val loginInterceptor: LoginInterceptor,
    private val loginAdminIdArgumentResolver: LoginAdminIdArgumentResolver,
    private val loginUserIdArgumentResolver: LoginUserIdArgumentResolver,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry
            .addInterceptor(loginInterceptor)
            .excludePathPatterns("/api/v1/dev/**")
            .excludePathPatterns("/api/v1/email/**")
            .excludePathPatterns("/api/v1/auth/**")
            .excludePathPatterns("/api/v1/users/register")
            .excludePathPatterns("/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs", "/error")
            .excludePathPatterns("/admin/users/login")
            .excludePathPatterns("/auth/**")
            .excludePathPatterns("/uket-events/**")
            .excludePathPatterns("/users/register")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("/favicon.ico")
            .addResourceLocations("classpath:/static/favicon.ico")
            .setCachePeriod(3600) // 1시간 캐시
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver?>) {
        resolvers.add(loginAdminIdArgumentResolver)
        resolvers.add(loginUserIdArgumentResolver)
        super.addArgumentResolvers(resolvers)
    }
}
