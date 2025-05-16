package uket.domain.eventregistration.service

import uket.domain.StatusState
import uket.domain.eventregistration.entity.EventRegistration
import uket.domain.eventregistration.entity.EventRegistrationStatus

interface EventRegistrationStatusState : StatusState<EventRegistrationStatus, EventRegistration>
