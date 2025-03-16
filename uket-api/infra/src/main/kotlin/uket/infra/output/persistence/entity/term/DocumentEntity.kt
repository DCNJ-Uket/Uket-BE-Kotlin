package uket.infra.output.persistence.entity.term

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
class DocumentEntity(
    var documentNo: Long = 0L,
    var name: String = "",
    var link: String = "",
    var version: Long = 0L
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "document_id")
    private var id: Long = 0L

}
