package uket.uket.facade

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uket.uket.domain.uketevent.service.UketEventService

@Service
@Transactional(readOnly = true)
class UketEventOrganizationService(
    val uketEventService: UketEventService,
    val organizationService: UketEventService,
) {
    fun findOrganizationNameByUketEventId(uketEventId: Long): String {
        val uketEvent = uketEventService.findById(uketEventId)
        val organization = organizationService.findById(uketEvent.organizationId)
        return organization.name
    }
}
