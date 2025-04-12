package uket.domain.admin.dto

import uket.domain.admin.entity.Organization

data class OrganizationDropdownItem(
    val id: Long,
    val name: String,
) {
    companion object {
        fun from(organization: Organization): OrganizationDropdownItem = OrganizationDropdownItem(
            id = organization.id,
            name = organization.name,
        )
    }
}
