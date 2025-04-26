package uket.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class DeletableEntity : BaseTimeEntity() {
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null

    fun updateDeletedAt() {
        this.deletedAt = LocalDateTime.now()
    }
}
