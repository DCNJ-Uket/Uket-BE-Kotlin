package uket.modules.qrcode.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.qr-code")
data class QRCodeProperties(
    val width: Int,
    val height: Int,
    val type: String,
)
