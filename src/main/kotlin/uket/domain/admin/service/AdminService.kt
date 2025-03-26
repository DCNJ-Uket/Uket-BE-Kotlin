package uket.uket.domain.organization.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.uket.domain.organization.dto.CreateAdminCommand
import uket.uket.domain.organization.dto.CreateAdminWithoutPasswordCommand
import uket.uket.domain.organization.entity.Admin
import uket.uket.domain.organization.repository.AdminRepository

@Service
@Transactional(readOnly = true)
class AdminService(
    val adminRepository: AdminRepository,
) {
    fun findById(adminId: Long): Admin {
        val admin = adminRepository.findByIdOrNull(adminId)
            ?: throw IllegalStateException("해당 어드민을 찾을 수 없습니다")
        return admin
    }

    fun findByEmail(email: String): Admin = adminRepository.findByEmail(email)
        ?: throw IllegalStateException("어드민의 이메일을 찾을 수 없습니다.")

    @Transactional
    fun createAdmin(createAdminCommand: CreateAdminCommand) {
        if (java.lang.Boolean.TRUE == adminRepository.existsByEmail(createAdminCommand.email)) {
            throw IllegalStateException("이미 가입된 어드민입니다.")
        }

        val admin = Admin(
            organization = createAdminCommand.organization,
            name = createAdminCommand.name,
            email = createAdminCommand.email,
            isSuperAdmin = false,
            password = createAdminCommand.password,
        )

        adminRepository.save(admin)
    }

    @Transactional
    fun delete(adminId: Long) {
        adminRepository.deleteById(adminId)
    }

    @Transactional
    fun createAdminWithoutPassword(createAdminWithoutPasswordCommand: CreateAdminWithoutPasswordCommand) {
        if (java.lang.Boolean.TRUE == adminRepository.existsByEmail(createAdminWithoutPasswordCommand.email)) {
            throw IllegalStateException("이미 가입된 어드민입니다.")
        }

        val admin = Admin(
            organization = createAdminWithoutPasswordCommand.organization,
            name = createAdminWithoutPasswordCommand.name,
            email = createAdminWithoutPasswordCommand.email,
            isSuperAdmin = false,
            password = null,
        )

        adminRepository.save(admin)
    }
}
