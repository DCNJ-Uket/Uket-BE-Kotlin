package uket.domain.admin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.admin.entity.Organization

interface OrganizationRepository : JpaRepository<Organization, Long> {
    fun findByName(name: String): Organization?

    @Query("""
    SELECT COUNT(o) > 0 FROM Organization o
    WHERE o.name = :name
    AND o.id NOT IN (
        SELECT a.organization.id FROM Admin a
    )
    """)
    fun existsByNameAndNotRegistered(name: String): Boolean
}
