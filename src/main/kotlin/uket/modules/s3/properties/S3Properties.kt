package uket.modules.s3.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.cloud.aws.s3")
data class S3Properties(
    val accessKey: String,
    val secretKey: String,
    val bucket: String,
)
