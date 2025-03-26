package uket.uket.domain.organization.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.uket.domain.organization.entity.Organization

interface OrganizationRepository : JpaRepository<Organization, Long>
