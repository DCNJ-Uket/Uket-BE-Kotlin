package uket.modules.qrcode.provider

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import org.springframework.stereotype.Component
import uket.modules.qrcode.properties.QRCodeProperties
import java.io.ByteArrayOutputStream
import java.io.IOException

@Component
class QRCodeProvider(
    private val qrCodeProperties: QRCodeProperties,
) {
    fun generateQRCodeByString(payload: String): ByteArrayOutputStream? = try {
        ByteArrayOutputStream().use { outputStream ->
            val matrix: BitMatrix = MultiFormatWriter().encode(
                payload,
                BarcodeFormat.QR_CODE,
                qrCodeProperties.width,
                qrCodeProperties.height
            )

            MatrixToImageWriter.writeToStream(matrix, qrCodeProperties.type, outputStream)
            outputStream
        }
    } catch (e: WriterException) {
        null
    } catch (e: IOException) {
        null
    }
}
