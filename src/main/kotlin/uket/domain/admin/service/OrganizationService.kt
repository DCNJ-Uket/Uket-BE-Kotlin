package uket.domain.admin.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uket.domain.admin.entity.Organization
import uket.domain.admin.repository.OrganizationRepository

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
) {
    fun findById(organizationId: Long): Organization {
        val organization = organizationRepository.findByIdOrNull(organizationId)
            ?: throw IllegalStateException("단체를 찾을 수 없습니다.")
        return organization
    }

    fun findByName(name: String): Organization {
        val organization = organizationRepository.findByName(name)
            ?: throw IllegalStateException("단체를 찾을 수 없습니다.")
        return organization
    }

    fun findAll(): List<Organization> = organizationRepository.findAll()
}
