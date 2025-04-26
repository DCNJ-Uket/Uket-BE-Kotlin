package uket.common

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

object TimeUtils {
    fun LocalDateTime.toKr(): ZonedDateTime {
        return this.atZone(ZoneId.of("Asia/Seoul"))
    }
}
