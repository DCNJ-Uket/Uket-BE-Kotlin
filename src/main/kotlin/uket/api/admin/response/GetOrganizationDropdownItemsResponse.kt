package uket.api.admin.response

import uket.domain.admin.dto.OrganizationDropdownItem

data class GetOrganizationDropdownItemsResponse(
    val items: List<OrganizationDropdownItem>,
)
