package uket.domain.admin.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import uket.domain.admin.entity.Admin

interface AdminRepository : JpaRepository<Admin, Long> {
    fun findByEmail(email: String): Admin?

    @Query("SELECT a.id FROM Admin a")
    fun findAdminIds(pageable: Pageable): List<Long>

    @Query("SELECT a FROM Admin a JOIN FETCH a.organization WHERE a.id IN :ids ORDER BY a.createdAt DESC")
    fun findAllByIdsOrderByCreatedAtDesc(ids: List<Long>): List<Admin>

    fun existsByEmail(email: String): Boolean
}
