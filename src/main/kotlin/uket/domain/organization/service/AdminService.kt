package uket.uket.domain.organization.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.uket.common.ErrorCode
import uket.uket.domain.organization.dto.CreateAdminCommand
import uket.uket.domain.organization.dto.CreateAdminWithoutPasswordCommand
import uket.uket.domain.organization.entity.Admin
import uket.uket.domain.organization.exception.OrganizationException
import uket.uket.domain.organization.repository.AdminRepository

@Service
@Transactional(readOnly = true)
class AdminService(
    val adminRepository: AdminRepository,
) {
    fun findByEmail(email: String): Admin = adminRepository.findByEmail(email)
        ?: throw OrganizationException(ErrorCode.NOT_FOUND_EMAIL_OF_ADMIN)

    @Transactional
    fun createAdmin(createAdminCommand: CreateAdminCommand) {
        if (java.lang.Boolean.TRUE == adminRepository.existsByEmail(createAdminCommand.email)) {
            throw OrganizationException(ErrorCode.ALREADY_EXIST_ADMIN)
        }

        val admin = Admin(
            _id = 0L,
            _organization = createAdminCommand.organization,
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
            throw OrganizationException(ErrorCode.ALREADY_EXIST_ADMIN)
        }

        val admin = Admin(
            _id = 0L,
            _organization = createAdminWithoutPasswordCommand.organization,
            name = createAdminWithoutPasswordCommand.name,
            email = createAdminWithoutPasswordCommand.email,
            isSuperAdmin = false,
            password = null,
        )

        adminRepository.save(admin)
    }
}
