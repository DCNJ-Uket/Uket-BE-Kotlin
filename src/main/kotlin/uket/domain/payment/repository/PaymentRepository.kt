package uket.domain.payment.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.payment.entity.Payment

interface PaymentRepository : JpaRepository<Payment, Long> {
    fun findByUketEventId(uketEventId: Long): Payment?

    @Query(
        """
            SELECT p FROM EntryGroup eg
            JOIN eg.uketEventRound uer
            JOIN uer.uketEvent ue
            JOIN Payment p ON p.uketEventId = ue.id
            WHERE eg.id = :entryGroupId
        """
    )
    fun findByEntryGroupId(entryGroupId: Long): Payment?
}
