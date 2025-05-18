package uket.domain.admin.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import uket.domain.admin.entity.Organization
import uket.domain.admin.repository.OrganizationRepository

class OrganizationServiceTest :
    DescribeSpec({
        isolationMode = IsolationMode.InstancePerLeaf

        val organizationRepository: OrganizationRepository = mockk<OrganizationRepository>()
        val organizationService: OrganizationService = OrganizationService(organizationRepository)

        val organization1: Organization = Organization(
            name = "organizationA",
            organizationImageId = "imagePathA",
        )
        val organization2: Organization = Organization(
            name = "organizationB",
            organizationImageId = "imagePathB",
        )

        describe("Organization을 ID로 조회") {

            context("Organization이 있으면") {
                every { organizationRepository.findByIdOrNull(organization1.id) } returns organization1

                it("해당 Organization을 반환한다") {
                    val findOrganization = organizationService.getById(organization1.id)
                    findOrganization.shouldNotBeNull()
                    findOrganization.name shouldBe organization1.name
                }
            }

            context("Organization이 없으면") {
                every { organizationRepository.findByIdOrNull(organization1.id) } returns null

                it("예외를 던진다") {
                    val exception =
                        shouldThrow<IllegalStateException> { organizationService.getById(organization1.id) }
                    exception.message shouldBe "단체를 찾을 수 없습니다."
                }
            }
        }

        describe("Organization을 name으로 조회") {

            context("Organization이 있으면") {
                every { organizationRepository.findByName(organization1.name) } returns organization1

                it("해당 Organization을 반환한다") {
                    val findOrganization = organizationService.getByName(organization1.name)
                    findOrganization.shouldNotBeNull()
                    findOrganization.name shouldBe organization1.name
                }
            }

            context("Organization이 없으면") {
                every { organizationRepository.findByName(organization1.name) } returns null

                it("예외를 던진다") {
                    val exception =
                        shouldThrow<IllegalStateException> { organizationService.getByName(organization1.name) }
                    exception.message shouldBe "단체를 찾을 수 없습니다."
                }
            }
        }

        describe("Organization 전체 조회") {

            context("Organization이 2개 이상이면") {
                every { organizationRepository.findAll() } returns listOf(organization1, organization2)

                it("전체 Organization 목록을 반환한다") {
                    val findOrganizations = organizationService.findAll()
                    findOrganizations.size shouldBe 2
                    findOrganizations.get(0).name shouldBe "organizationA"
                }
            }
            context("Organization이 1개면") {
                every { organizationRepository.findAll() } returns listOf(organization1)

                it("해당 Organization을 반환한다(리스트 형태로)") {
                    val findOrganizations = organizationService.findAll()
                    findOrganizations.size shouldBe 1
                    findOrganizations.get(0).name shouldBe "organizationA"
                }
            }

            context("Organization이 없으면") {
                every { organizationRepository.findAll() } returns listOf()

                it("빈 리스트를 반환한다") {
                    val findOrganizations = organizationService.findAll()
                    findOrganizations.size shouldBe 0
                }
            }
        }
    })
