package uket.infra.output.persistence.entity.admin

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class OrganizationEntity(
    var name: String = "",
    var organizationImagePath: String= "",
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    @Id
    @GeneratedValue
    @Column(name = "organization_id")
    private var id: Long = 0L

}
