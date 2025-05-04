package uket.auth.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import uket.auth.filter.JwtAccessDeniedHandler
import uket.auth.filter.JwtAuthenticationEntryPoint
import uket.auth.filter.JwtFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtFilter: JwtFilter,
    private val accessDeniedHandler: JwtAccessDeniedHandler,
    private val entryPoint: JwtAuthenticationEntryPoint,
) {
    @Bean
    @Throws(Exception::class)
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager = configuration.authenticationManager

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { cors ->
                cors.configurationSource {
                    val configuration = CorsConfiguration()
                    configuration.allowedMethods = listOf(
                        *ALLOWED_METHOD_NAMES
                            .split(",".toRegex())
                            .dropLastWhile { it.isEmpty() }
                            .toTypedArray(),
                    )
                    configuration.setAllowedOriginPatterns(listOf("*"))
                    configuration.allowedHeaders = listOf("*")
                    configuration.maxAge = 3600L

                    configuration.exposedHeaders = listOf(
                        HttpHeaders.AUTHORIZATION,
                    )
                    configuration
                }
            }.csrf {
                it.disable()
            }.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { exceptionHandlerManagement ->
                exceptionHandlerManagement
                    .authenticationEntryPoint(entryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            }.authorizeHttpRequests { registry ->
                registry
                    .requestMatchers("/favicon.ico")
                    .permitAll()
                    .requestMatchers("/error")
                    .permitAll()
            }.authorizeHttpRequests { registry ->
                registry // actuator, rest docs 경로, 실무에서는 상황에 따라 적절한 접근제어 필요
                    .requestMatchers("/actuator/*")
                    .permitAll()
                    .requestMatchers("/swagger-ui.html")
                    .permitAll()
                    .requestMatchers("/swagger-ui/**")
                    .permitAll()
                    .requestMatchers("/v3/api-docs/**")
                    .permitAll()
            }.authorizeHttpRequests { registry ->
                registry
                    .requestMatchers("/api/v1/auth")
                    .permitAll()
                    .requestMatchers("/api/v1/auth/login/**")
                    .permitAll()
            }.authorizeHttpRequests { registry ->
                registry
                    .requestMatchers("/api/v1/dev/token")
                    .permitAll()
                    .requestMatchers("/api/v1/dev/token/registered")
                    .permitAll()
            }.authorizeHttpRequests { registry ->
                registry
                    .requestMatchers("/admin/users/login")
                    .permitAll()
                    .requestMatchers("/auth/**")
                    .permitAll()
                    .requestMatchers("/uket-events/*/rounds/**")
                    .authenticated()
                    .requestMatchers("/rounds/*/entry-groups/**")
                    .authenticated()
                    .requestMatchers("/uket-events/*/reservation")
                    .authenticated()
                    .requestMatchers("/uket-events/**")
                    .permitAll()
            }.authorizeHttpRequests { registry ->
                registry
                    .anyRequest()
                    .authenticated()
            }.sessionManagement { session ->
                session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

        return http.build()
    }

    companion object {
        private const val ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH"
    }
}
