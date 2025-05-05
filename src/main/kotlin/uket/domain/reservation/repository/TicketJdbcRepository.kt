package uket.uket.domain.reservation.repository

import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import uket.domain.reservation.entity.Ticket
import java.sql.PreparedStatement

@Component
class TicketJdbcRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun saveAllBatch(tickets: List<Ticket>) {
        val sql =
            """
                INSERT INTO ticket (user_id, entry_group_id, status, ticket_no, enter_at, created_at, updated_at, deleted_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """

        jdbcTemplate.batchUpdate(
            sql,
            object : BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, i: Int) {
                    val ticket = tickets[i]
                    ps.setLong(1, ticket.userId)
                    ps.setLong(2, ticket.entryGroupId)
                    ps.setString(3, ticket.status.name)
                    ps.setString(4, ticket.ticketNo)
                    ps.setObject(5, ticket.enterAt)
                    ps.setObject(6, ticket.createdAt)
                    ps.setObject(7, ticket.updatedAt)
                    ps.setObject(8, ticket.deletedAt)
                }

                override fun getBatchSize(): Int = tickets.size
            }
        )
    }
}
