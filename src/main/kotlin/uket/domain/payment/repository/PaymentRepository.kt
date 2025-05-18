package uket.domain.payment.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.payment.entity.Payment

interface PaymentRepository : JpaRepository<Payment, Long> {
    fun findByOrganizationId(uketEventId: Long): Payment?

    @Query(
        """
            SELECT p FROM UketEvent ue
            JOIN Payment p ON p.organizationId = ue.organizationId
            WHERE ue.id = :eventId
        """
    )
    fun findByEventId(eventId: Long): Payment?
}
