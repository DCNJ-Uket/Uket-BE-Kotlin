package uket.common.aop.masking

object MaskingUtil {

    fun maskingOf(type: MaskingType, value: String?): String {
        if (value.isNullOrBlank()) return value ?: ""
        return when (type) {
            MaskingType.NAME -> nameMaskOf(value)
            MaskingType.PHONE -> phoneNumberMaskOf(value)
        }
    }

    private fun nameMaskOf(value: String): String {
        // 홍*동 마스킹: 첫글자, 마지막글자 제외 마스킹
        return if (value.length <= 2) {
            value.first() + "*"
        } else {
            val maskedMiddle = "*".repeat(value.length - 2)
            value.first() + maskedMiddle + value.last()
        }
    }

    private fun phoneNumberMaskOf(value: String): String {
        val cleaned = value.replace("-", "")
        val regex = Regex("(\\d{3})(\\d{4})(\\d{4})")
        return regex.replace(cleaned) { matchResult ->
            val (part1, _, part3) = matchResult.destructured
            "$part1****$part3"
        }
    }
}
