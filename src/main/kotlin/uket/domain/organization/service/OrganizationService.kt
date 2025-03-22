package uket.uket.domain.organization.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import uket.uket.common.ErrorCode
import uket.uket.domain.organization.entity.Organization
import uket.uket.domain.organization.exception.OrganizationException
import uket.uket.domain.organization.repository.OrganizationRepository

@Service
class OrganizationService(
    val organizationRepository: OrganizationRepository,
) {
    fun findById(organizationId: Long): Organization = organizationRepository.findByIdOrNull(organizationId)
        ?: throw OrganizationException(ErrorCode.NOT_FOUND_ORGANIZATION)
}
