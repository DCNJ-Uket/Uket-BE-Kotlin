package uket.uket.domain.uketevent.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import uket.domain.admin.entity.QOrganization.organization
import uket.domain.uketevent.entity.QUketEvent.uketEvent

class UketEventRepositoryCustomImpl(
    val queryFactory: JPAQueryFactory,
) : UketEventRepositoryCustom {
    override fun findOrganizationNameByUketEventId(uketEventId: Long): String {
        val name = queryFactory
            .select(organization.name)
            .from(organization)
            .join(uketEvent)
            .on(organization.id.eq(uketEvent.organizationId))
            .where(uketEvent.id.eq(uketEventId))
            .fetchOne() ?: ""
        return name
    }
}
