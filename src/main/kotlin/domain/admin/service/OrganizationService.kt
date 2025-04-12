package uket.domain.admin.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.domain.admin.dto.OrganizationDropdownItem
import uket.domain.admin.entity.Organization
import uket.domain.admin.repository.OrganizationRepository

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
) {
    @Transactional(readOnly = true)
    fun getById(organizationId: Long): Organization {
        val organization = organizationRepository.findByIdOrNull(organizationId)
            ?: throw IllegalStateException("단체를 찾을 수 없습니다.")
        return organization
    }

    @Transactional(readOnly = true)
    fun getByName(name: String): Organization {
        val organization = organizationRepository.findByName(name)
            ?: throw IllegalStateException("단체를 찾을 수 없습니다.")
        return organization
    }

    fun findAll(): List<Organization> = organizationRepository.findAll()

    fun findAllIdAndNames(): List<OrganizationDropdownItem> {
        val organizations = organizationRepository.findAll()
        return organizations.stream().map { o -> OrganizationDropdownItem.from(o) }.toList()
    }
}
