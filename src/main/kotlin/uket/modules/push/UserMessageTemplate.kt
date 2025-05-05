package uket.uket.modules.push

/**
 *  링크: https://uker.co.kr/{link}
 */
sealed class UserMessageTemplate(
    val code: String,
) {
    interface Command

    abstract fun makeContext(command: Command): Map<String, String>

    data object 관람일당일안내알림톡 : UserMessageTemplate("uket_event_today") {
        private const val 예매내역목록_LINK_PATH = "ticket-list"

        override fun makeContext(command: Command): Map<String, String> {
            return with(command as 관람일당일안내Command) {
                mapOf(
                    "이름" to userName,
                    "행사명" to eventName,
                    "행사타입" to eventType,
                    "행사일시" to 행사일시,
                    "행사장소" to 행사장소,
                    LINK_CONTEXT_KEY to 예매내역목록_LINK_PATH
                )
            }
        }

        data class 관람일당일안내Command(
            val userName: String,
            val eventName: String,
            val eventType: String,
            val 행사일시: String,
            val 행사장소: String,
        ) : Command
    }

    data object 티켓취소알림톡 : UserMessageTemplate("uket_ticket_cancel") {
        override fun makeContext(command: Command): Map<String, String> {
            return with(command as 티켓취소알림톡Command) {
                mapOf(
                    "이름" to userName,
                    "행사명" to eventName,
                    "주최명" to organizationName,
                    "행사타입" to eventType,
                    "예매번호" to 예매번호
                )
            }
        }

        data class 티켓취소알림톡Command(
            val userName: String,
            val eventName: String,
            val organizationName: String,
            val eventType: String,
            val 예매번호: String,
        ) : Command
    }

    companion object {
        private const val LINK_CONTEXT_KEY = "link"
    }
}
