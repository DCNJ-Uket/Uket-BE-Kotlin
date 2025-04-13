package uket.domain.admin.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.admin.dto.AdminWithOrganizationDto
import uket.domain.admin.dto.RegisterAdminCommand
import uket.domain.admin.dto.RegisterAdminWithoutPasswordCommand
import uket.domain.admin.entity.Admin
import uket.domain.admin.entity.Organization
import uket.domain.admin.repository.AdminRepository

@Service
@Transactional(readOnly = true)
class AdminService(
    private val adminRepository: AdminRepository,
) {
    fun getById(adminId: Long): Admin {
        val admin = adminRepository.findByIdOrNull(adminId)
            ?: throw IllegalStateException("해당 어드민을 찾을 수 없습니다")
        return admin
    }

    fun getByEmail(email: String): Admin = adminRepository.findByEmail(email)
        ?: throw IllegalStateException("해당 어드민을 찾을 수 없습니다")

    fun findAdminsWithOrganizationIdAndNameByPage(pageRequest: PageRequest): Page<AdminWithOrganizationDto> {
        val ids = adminRepository.findAdminIds(pageRequest)
        val admins = adminRepository.findAllByIdsOrderByCreatedAtDesc(ids)
        val adminItems = admins
            .stream()
            .map { AdminWithOrganizationDto.from(it) }
            .toList()
        val count = adminRepository.count()
        return PageImpl(adminItems, pageRequest, count)
    }

    @Transactional
    fun registerAdmin(registerAdminCommand: RegisterAdminCommand) {
        check(adminRepository.existsByEmail(registerAdminCommand.email).not()) { "이미 가입된 어드민입니다." }

        val admin = Admin(
            organization = registerAdminCommand.organization,
            name = registerAdminCommand.name,
            email = registerAdminCommand.email,
            isSuperAdmin = false,
            password = registerAdminCommand.password,
        )

        adminRepository.save(admin)
    }

    @Transactional
    fun registerAdminWithoutPassword(command: RegisterAdminWithoutPasswordCommand, organization: Organization): Admin {
        check(adminRepository.existsByEmail(command.email).not()) { "이미 가입된 어드민입니다." }

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
    fun updatePassword(email: String, password: String): Admin {
        val admin: Admin = getByEmail(email)
        admin.updatePassword(password)
        return adminRepository.save(admin)
    }

    @Transactional
    fun deleteAdmin(adminId: Long) {
        this.getById(adminId)
        adminRepository.deleteById(adminId)
    }

    fun checkDuplicateEmail(email: String) {
        check(adminRepository.existsByEmail(email).not()) { "이미 가입된 사용자입니다." }
    }
}
