package uket.infra.output.persistence.entity.admin

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import uket.infra.output.persistence.entity.BaseTimeEntity
import java.time.LocalDateTime

@Entity
class OrganizationEntity(
    var name: String = "",
    var organizationImagePath: String= ""
) : BaseTimeEntity() {
    @Id
    @GeneratedValue
    @Column(name = "organization_id")
    private var id: Long = 0L

}
