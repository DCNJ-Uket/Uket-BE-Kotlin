package uket.domain.uketevent.repository

interface UketEventRepositoryCustom {
    fun findOrganizationNameByUketEventId(uketEventId: Long): String
}
