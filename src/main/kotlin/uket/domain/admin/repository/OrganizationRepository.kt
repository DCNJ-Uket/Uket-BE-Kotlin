package uket.domain.admin.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.domain.admin.entity.Organization

interface OrganizationRepository : JpaRepository<Organization, Long> {
//    fun findOrganizationNameByUketEventId(): String
}
