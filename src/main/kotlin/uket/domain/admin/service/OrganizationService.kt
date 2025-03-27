package uket.domain.organization.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uket.domain.organization.entity.Organization
import uket.domain.organization.repository.OrganizationRepository

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
) {
    fun findById(organizationId: Long): Organization {
        val organization = organizationRepository.findByIdOrNull(organizationId)
            ?: throw IllegalStateException("단체를 찾을 수 없습니다.")
        return organization
    }
}
