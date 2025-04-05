package uket.domain.admin.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.admin.dto.RegisterAdminCommand
import uket.domain.admin.dto.RegisterAdminWithoutPasswordCommand
import uket.domain.admin.entity.Admin
import uket.domain.admin.entity.Organization
import uket.domain.admin.repository.AdminRepository
import java.lang.Boolean

@Service
@Transactional(readOnly = true)
class AdminService(
    private val adminRepository: AdminRepository,
) {
    fun findById(adminId: Long): Admin {
        val admin = adminRepository.findByIdOrNull(adminId)
            ?: throw IllegalStateException("해당 어드민을 찾을 수 없습니다")
        return admin
    }

    fun findByEmail(email: String): Admin = adminRepository.findByEmail(email)
        ?: throw IllegalStateException("해당 어드민을 찾을 수 없습니다")

    @Transactional
    fun registerAdmin(command: RegisterAdminCommand) {
        check(Boolean.TRUE != adminRepository.existsByEmail(command.email)) { "이미 가입된 어드민입니다." }

        val admin = Admin(
            organization = command.organization,
            name = command.name,
            email = command.email,
            isSuperAdmin = false,
            password = command.password,
        )

        adminRepository.save(admin)
    }

    @Transactional
    fun registerAdminWithoutPassword(command: RegisterAdminWithoutPasswordCommand, organization: Organization):Admin {
        check(Boolean.TRUE != adminRepository.existsByEmail(command.email)) { "이미 가입된 어드민입니다." }

        val isSuperAdmin = when (command.authority) {
            "관리자" -> true
            else -> false
        }

        val admin = Admin(
            organization = organization,
            name = command.name,
            email = command.email,
            isSuperAdmin = isSuperAdmin,
            password = null,
        )

        return adminRepository.save(admin)
    }

    @Transactional
    fun updatePassword(email: String, password: String):Admin {
        val admin: Admin = findByEmail(email)
        admin.updatePassword(password)
        return adminRepository.save(admin)
    }

    @Transactional
    fun deleteAdmin(adminId: Long) {
        adminRepository.deleteById(adminId)
    }

    fun checkDuplicateEmail(email: String) {
        check(Boolean.TRUE != adminRepository.existsByEmail(email)) { "이미 가입된 사용자입니다." }
    }
}
