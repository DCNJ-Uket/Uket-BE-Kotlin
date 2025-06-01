package uket.domain.eventregistration.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import uket.domain.eventregistration.entity.EventRegistration

interface EventRegistrationRepository : JpaRepository<EventRegistration, Long> {
    fun findAllByOrganizationId(organizationId: Long, pageable: Pageable): Page<EventRegistration>
}
