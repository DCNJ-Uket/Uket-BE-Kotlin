package uket.uket.modules.push

/**
 *  링크: https://uker.co.kr/{link}
 */
sealed class UserMessageTemplate(
    val code: String,
    val referrer: String = "",
) {
    interface Command

    abstract fun makeContext(command: Command): Map<String, String>

    data object 예매완료알림톡 : UserMessageTemplate(
        code = "uket_ticket_paid",
        referrer = REFERRER_TICKET_PAID
    ) {
        private const val 예매내역목록_LINK_PATH = "ticket-list"

        override fun makeContext(command: Command): Map<String, String> = with(command as 예매완료알림Command) {
            mapOf(
                "이름" to userName,
                "행사명" to eventName,
                "행사타입" to eventType,
                "행사일자" to 행사일자,
                "행사시간" to 행사시간,
                "행사장소" to 행사장소,
                "예매번호" to ticketNo,
                LINK_CONTEXT_KEY to 예매내역목록_LINK_PATH
            )
        }

        data class 예매완료알림Command(
            val userName: String,
            val eventName: String,
            val eventType: String,
            val ticketNo: String,
            val 행사일자: String,
            val 행사시간: String,
            val 행사장소: String,
        ) : Command
    }

    data object 결제안내알림톡 : UserMessageTemplate(
        code = "uket_ticket_payment",
        referrer = REFERRER_TICKET_PAYMENT
    ) {
        private const val 예매내역목록_LINK_PATH = "ticket-list"

        override fun makeContext(command: Command): Map<String, String> = with(command as 결제안내Command) {
            mapOf(
                "행사명" to eventName,
                "금액" to ticketPrice,
                "은행이름" to bankName,
                "계좌번호" to accountNumber,
                "예금주" to depositorName,
                "이름" to userName,
                LINK_CONTEXT_KEY to 예매내역목록_LINK_PATH
            )
        }

        data class 결제안내Command(
            val eventName: String,
            val ticketPrice: String,
            val bankName: String,
            val accountNumber: String,
            val depositorName: String,
            val userName: String,
        ) : Command
    }

    data object 관람일당일안내알림톡 : UserMessageTemplate(
        code = "uket_event_today",
        referrer = REFERRER_EVENT_TODAY
    ) {
        private const val 예매내역목록_LINK_PATH = "ticket-list"

        override fun makeContext(command: Command): Map<String, String> = with(command as 관람일당일안내Command) {
            mapOf(
                "이름" to userName,
                "행사명" to eventName,
                "행사타입" to eventType,
                "행사일시" to 행사일시,
                "행사장소" to 행사장소,
                LINK_CONTEXT_KEY to 예매내역목록_LINK_PATH
            )
        }

        data class 관람일당일안내Command(
            val userName: String,
            val eventName: String,
            val eventType: String,
            val 행사일시: String,
            val 행사장소: String,
        ) : Command
    }

    data object 관람일하루전안내알림톡 : UserMessageTemplate(
        code = "uket_event_remind",
        referrer = REFERRER_EVENT_REMIND
    ) {
        private const val 예매내역목록_LINK_PATH = "ticket-list"

        override fun makeContext(command: Command): Map<String, String> = with(command as 관람일하루전안내Command) {
            mapOf(
                "이름" to userName,
                "행사명" to eventName,
                "행사타입" to eventType,
                "행사일시" to 행사일시,
                "행사장소" to 행사장소,
                LINK_CONTEXT_KEY to 예매내역목록_LINK_PATH
            )
        }

        data class 관람일하루전안내Command(
            val userName: String,
            val eventName: String,
            val eventType: String,
            val 행사일시: String,
            val 행사장소: String,
        ) : Command
    }

    data object 티켓취소알림톡 : UserMessageTemplate(
        code = "uket_ticket_cancel",
        referrer = REFERRER_TICKET_CANCEL
    ) {
        override fun makeContext(command: Command): Map<String, String> = with(command as 티켓취소알림Command) {
            mapOf(
                "이름" to userName,
                "행사명" to eventName,
                "주최명" to organizationName,
                "행사타입" to eventType,
                "예매번호" to 예매번호
            )
        }

        data class 티켓취소알림Command(
            val userName: String,
            val eventName: String,
            val organizationName: String,
            val eventType: String,
            val 예매번호: String,
        ) : Command
    }

    companion object {
        private const val LINK_CONTEXT_KEY = "link"

        private const val REFERRER_TICKET_CANCEL = "NK3MiV1n"
        private const val REFERRER_EVENT_REMIND = "WjsTGIUz"
        private const val REFERRER_EVENT_TODAY = "3x0jJanU"
        private const val REFERRER_TICKET_PAID = "RqqcPBKL"
        private const val REFERRER_TICKET_PAYMENT = "JwuAfMZC"

        const val TEMPLATE_MAXIMUM_LENGTH = 14
    }
}
