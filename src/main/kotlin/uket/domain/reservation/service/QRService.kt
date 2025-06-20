package uket.domain.reservation.service

import org.springframework.stereotype.Service
import uket.auth.jwt.JwtTicketUtil
import uket.common.ErrorLevel
import uket.common.PublicException
import uket.modules.qrcode.provider.QRCodeProvider
import java.io.ByteArrayOutputStream
import java.io.IOException

@Service
class QRService(
    private val qrCodeProvider: QRCodeProvider,
    private val jwtTicketUtil: JwtTicketUtil,
) {
    fun generateTicketQRCode(ticketId: Long): ByteArray {
        val ticketToken = jwtTicketUtil.createTicketToken(ticketId)
        try {
            qrCodeProvider.generateQRCodeByString(ticketToken).use { outputStream ->
                validateQRCode(outputStream)
                return outputStream!!.toByteArray()
            }
        } catch (e: IOException) {
            throw PublicException(
                publicMessage = "QR 코드 생성에 실패했습니다. 잠시 후 다시 시도해주세요.",
                systemMessage = "티켓 QR 코드 생성 오류 | ${e.message}",
                title = "티켓 QR 코드 생성 실패",
                errorLevel = ErrorLevel.ERROR
            )
        }
    }

    private fun validateQRCode(outputStream: ByteArrayOutputStream?) {
        if (outputStream == null) {
            throw PublicException(
                publicMessage = "QR 코드 생성에 실패했습니다. 잠시 후 다시 시도해주세요.",
                systemMessage = "티켓 QR 코드 생성 오류 | outputStream=$outputStream",
                title = "티켓 QR 코드 생성 실패",
                errorLevel = ErrorLevel.ERROR
            )
        }
    }
}
