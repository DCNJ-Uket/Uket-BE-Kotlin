package uket.domain.admin.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.domain.admin.entity.Admin

interface AdminRepository : JpaRepository<Admin, Long> {
    fun findByEmail(email: String): Admin?

    fun existsByEmail(email: String): Boolean
}
