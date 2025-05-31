package uket.domain.uketEvent.util

import uket.domain.uketevent.entity.EntryGroup
import uket.domain.uketevent.entity.UketEvent
import uket.domain.uketevent.entity.UketEventRound
import java.time.LocalDateTime

class SqlGenerator {
    companion object {
        fun toInsertSqlForEntryGroup(entryGroup: EntryGroup): String {
            val tableName = "entry_group"

            val columns = mutableListOf<String>()
            val values = mutableListOf<String>()

            // 직접 필드 접근 (중첩 필드도 분해해서 처리)
            with(entryGroup) {
                // 일반 필드
                columns += listOf(
                    "uket_event_round_id",
                    "entry_group_name",
                    "entry_start_datetime",
                    "entry_end_datetime",
                    "ticket_count",
                    "total_ticket_count",
                    "created_at",
                    "updated_at"
                )

                values += listOf(
                    uketEventRound.id,
                    entryGroupName,
                    entryStartDateTime,
                    entryEndDateTime,
                    ticketCount,
                    totalTicketCount,
                    createdAt,
                    updatedAt
                ).map { toSqlValue(it) }
            }

            return "INSERT INTO $tableName (${columns.joinToString(", ")}) VALUES (${values.joinToString(", ")});"
        }

        fun toInsertSqlForUketEventRound(uketEventRound: UketEventRound): String {
            val tableName = "uket_event_round"

            val columns = mutableListOf<String>()
            val values = mutableListOf<String>()

            // 직접 필드 접근 (중첩 필드도 분해해서 처리)
            with(uketEventRound) {
                // 일반 필드
                columns += listOf(
                    "uket_event_id",
                    "event_round_datetime",
                    "ticketing_start_datetime",
                    "ticketing_end_datetime",
                    "created_at",
                    "updated_at"
                )

                values += listOf(
                    uketEvent!!.id,
                    eventRoundDateTime,
                    ticketingStartDateTime,
                    ticketingEndDateTime,
                    createdAt,
                    updatedAt
                ).map { toSqlValue(it) }
            }

            return "INSERT INTO $tableName (${columns.joinToString(", ")}) VALUES (${values.joinToString(", ")});"
        }

        fun toInsertSqlForUketEvent(event: UketEvent): String {
            val tableName = "uket_event"

            val columns = mutableListOf<String>()
            val values = mutableListOf<String>()

            // 직접 필드 접근 (중첩 필드도 분해해서 처리)
            with(event) {
                // 일반 필드
                columns += listOf(
                    "organization_id",
                    "event_name",
                    "event_type",
                    "location",
                    "total_ticket_count",
                    "ticket_price",
                    "event_image_id",
                    "thumbnail_image_id",
                    "first_round_datetime",
                    "last_round_datetime",
                    "created_at",
                    "updated_at"
                )

                values += listOf(
                    organizationId,
                    eventName,
                    eventType.name,
                    location,
                    totalTicketCount,
                    ticketPrice,
                    eventImageId,
                    thumbnailImageId,
                    firstRoundDateTime,
                    lastRoundDateTime,
                    createdAt,
                    updatedAt
                ).map { toSqlValue(it) }

                // Embedded: EventDetails -> information, caution, contact_type, contact_content
                columns += listOf("information", "caution", "contact_type", "contact_content")
                values += listOf(
                    details.information,
                    details.caution,
                    details.contact.type.name,
                    details.contact.content
                ).map { toSqlValue(it) }
            }

            return "INSERT INTO $tableName (${columns.joinToString(", ")}) VALUES (${values.joinToString(", ")});"
        }

        fun toSqlValue(value: Any?): String = when (value) {
            null -> "NULL"
            is String -> "'${value.replace("'", "''")}'"
            is Enum<*> -> "'${value.name}'"
            is LocalDateTime -> "'$value'"
            is Boolean -> if (value) "1" else "0"
            else -> value.toString()
        }
    }
}
