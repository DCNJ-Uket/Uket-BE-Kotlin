package uket.domain.terms.repository

import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import uket.domain.terms.entity.TermSign
import java.sql.PreparedStatement

@Component
class TermSignJdbcRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    fun batchSaveAll(termSigns: List<TermSign>) {
        val sql =
            """
                INSERT INTO term_sign (terms_id, document_id, user_id, is_agreed, agree_at, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """

        jdbcTemplate.batchUpdate(
            sql,
            object : BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, i: Int) {
                    val sign = termSigns[i]
                    ps.setLong(1, sign.terms.id)
                    ps.setLong(2, sign.document.id)
                    ps.setLong(3, sign.userId)
                    ps.setBoolean(4, sign.isAgreed)
                    ps.setObject(5, sign.agreeAt)
                    ps.setObject(6, sign.createdAt)
                    ps.setObject(7, sign.updatedAt)
                }

                override fun getBatchSize(): Int = termSigns.size
            }
        )
    }
}
