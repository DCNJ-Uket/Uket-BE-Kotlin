package uket.uket.domain.eventregistration.service

import uket.domain.eventregistration.entity.EventRegistration
import uket.domain.eventregistration.entity.EventRegistrationStatus
import uket.uket.domain.StatusState

interface EventRegistrationStatusState : StatusState<EventRegistrationStatus, EventRegistration>
