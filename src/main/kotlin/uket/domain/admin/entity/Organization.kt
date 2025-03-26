package uket.uket.domain.organization.entity

import jakarta.persistence.*
import uket.uket.domain.BaseTimeEntity

@Entity
@Table(name = "organization")
class Organization(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val name: String,
    val organizationImagePath: String?,
) : BaseTimeEntity()
