package uket.modules.s3.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import uket.modules.s3.properties.S3Properties

@Configuration
class S3Config(
    private val s3Properties: S3Properties,
) {
    @Bean
    fun preSigner(): S3Presigner {
        val credentials = AwsBasicCredentials.create(s3Properties.accessKey, s3Properties.secretKey)

        return S3Presigner.builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(Region.AP_NORTHEAST_2)
            .build()
    }
}
