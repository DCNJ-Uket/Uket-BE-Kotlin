package uket.domain.admin.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.admin.entity.Admin

interface AdminRepository : JpaRepository<Admin, Long> {
    fun findByEmail(email: String): Admin?

    @Query("SELECT a FROM Admin a JOIN FETCH a.organization")
    fun findAllWithOrganizationName(): List<Admin>

    fun existsByEmail(email: String): Boolean?
}
