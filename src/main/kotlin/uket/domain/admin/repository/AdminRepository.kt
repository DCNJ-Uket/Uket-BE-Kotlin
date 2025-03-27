package uket.domain.organization.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.domain.organization.entity.Admin

interface AdminRepository : JpaRepository<Admin, Long> {
    fun findByEmail(email: String): Admin?

    fun existsByEmail(email: String): Boolean?
}
