package uket.auth.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@OpenAPIDefinition(
    servers = [Server(url = "/", description = "Default Server URL")],
    info = Info(
        title = "Uket 백엔드 API 명세",
        description = "springdoc을 이용한 Swagger API 문서입니다.",
        version = "1.0",
        contact = Contact(name = "springdoc 공식문서", url = "https://springdoc.org/"),
    ),
)
@Configuration
class SwaggerConfig {
    @Bean
    fun customOpenAPI(): OpenAPI = OpenAPI()
        .components(Components().addSecuritySchemes("JWT", bearerAuth()))

    fun bearerAuth(): SecurityScheme = SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("Bearer")
        .bearerFormat("JWT")
        .`in`(SecurityScheme.In.HEADER)
        .name(HttpHeaders.AUTHORIZATION)
}
