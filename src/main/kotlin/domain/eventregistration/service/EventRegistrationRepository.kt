package uket.domain.eventregistration.service

import org.springframework.data.jpa.repository.JpaRepository
import uket.domain.eventregistration.entity.EventRegistration

interface EventRegistrationRepository : JpaRepository<EventRegistration, Long>
