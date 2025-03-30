package uket.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import uket.domain.user.entity.User
import uket.domain.user.enums.Platform

interface UserRepository : JpaRepository<User, Long> {
    fun findByPlatformAndPlatformId(platform: Platform, platformId: String): User?

    fun existsByEmail(email: String): Boolean?
}
