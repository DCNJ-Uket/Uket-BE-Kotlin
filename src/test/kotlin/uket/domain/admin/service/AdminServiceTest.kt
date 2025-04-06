package uket.domain.admin.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import uket.domain.admin.dto.RegisterAdminCommand
import uket.domain.admin.dto.RegisterAdminWithoutPasswordCommand
import uket.domain.admin.entity.Admin
import uket.domain.admin.entity.Organization
import uket.domain.admin.repository.AdminRepository

class AdminServiceTest :
    DescribeSpec({
        isolationMode = IsolationMode.InstancePerLeaf

        val adminRepository: AdminRepository = mockk<AdminRepository>()
        val adminService: AdminService = AdminService(adminRepository)

        val organization: Organization = Organization(
            name = "organizationA",
            organizationImagePath = "imagePath",
        )
        val admin1: Admin = Admin(
            organization = organization,
            name = "adminA",
            email = "emailA",
            password = "passwordA",
            isSuperAdmin = false,
        )
        val admin2: Admin = Admin(
            organization = organization,
            name = "adminB",
            email = "emailB",
            password = "passwordB",
            isSuperAdmin = false,
        )

        describe("Admin을 ID로 조회") {

            context("Admin이 있으면") {
                every { adminRepository.findByIdOrNull(admin1.id) } returns admin1

                it("해당 Admin을 반환한다") {
                    val findAdmin = adminService.findById(admin1.id)
                    admin1.shouldNotBeNull()
                    findAdmin.name shouldBe admin1.name
                }
            }

            context("Admin이 없으면") {
                every { adminRepository.findByIdOrNull(admin1.id) } returns null

                it("예외를 던진다") {
                    val exception = shouldThrow<IllegalStateException> { adminService.findById(admin1.id) }
                    exception.message shouldBe "해당 어드민을 찾을 수 없습니다"
                }
            }
        }

        describe("Admin을 email로 조회") {
            context("Admin이 있으면") {
                every { adminRepository.findByEmail(admin1.email) } returns admin1
                it("해당 Admin을 반환한다") {
                    val findAdmin = adminService.findByEmail(admin1.email)
                    admin1.shouldNotBeNull()
                    findAdmin.email shouldBe admin1.email
                }
            }
            context("Admin이 없으면") {
                every { adminRepository.findByEmail(admin1.email) } returns null
                it("예외를 던진다") {
                    val exception = shouldThrow<IllegalStateException> { adminService.findByEmail(admin1.email) }
                    exception.message shouldBe "해당 어드민을 찾을 수 없습니다"
                }
            }
        }

        describe("Admin 전체 조회") {
            val pageRequset = PageRequest.of(1, 2)
            context("Admin이 2개 이상이면") {
                val list = listOf(admin1.id, admin2.id)
                every { adminRepository.findAdminIds(pageRequset) } returns list
                every { adminRepository.findAllByIdsOrderByCreatedAtDesc(list) } returns listOf(admin2, admin1)
                every { adminRepository.count() } returns 2
                it("어드민 전체 목록을 반환한다") {
                    val adminPage = adminService.findAdminsByPage(pageRequset)
                    adminPage.content.size shouldBe 2
                    adminPage.content.get(0).name shouldBe "adminB"
                    adminPage.content.get(1).name shouldBe "adminA"
                }
            }
            context("Admin이 1개면") {
                val list = listOf(admin1.id)
                every { adminRepository.findAdminIds(pageRequset) } returns list
                every { adminRepository.findAllByIdsOrderByCreatedAtDesc(list) } returns listOf(admin1)
                every { adminRepository.count() } returns 1
                it("어드민 전체 목록(이지만 1개)을 반환한다") {
                    val adminPage = adminService.findAdminsByPage(pageRequset)
                    adminPage.content.size shouldBe 1
                    adminPage.content.get(0).name shouldBe "adminA"
                }
            }
            context("Admin이 0개면") {
                val list: List<Long> = listOf()
                every { adminRepository.findAdminIds(pageRequset) } returns list
                every { adminRepository.findAllByIdsOrderByCreatedAtDesc(list) } returns listOf()
                every { adminRepository.count() } returns 0
                it("어드민 전체 목록(이지만 빈 리스트)을 반환한다") {
                    val adminPage = adminService.findAdminsByPage(pageRequset)
                    adminPage.content.size shouldBe 0
                }
            }
        }

        describe("Admin 생성 요청") {
            val registerAdminCommand: RegisterAdminCommand = RegisterAdminCommand(
                organization = organization,
                name = "newAdmin",
                email = "newEmail",
                password = "newPassword",
            )
            context("Admin이 이미 존재하지 않으면") {
                every { adminRepository.existsByEmail(registerAdminCommand.email) } returns false
                every { adminRepository.save(any()) } returns null
                it("Admin을 생성한다") {
                    adminService.registerAdmin(registerAdminCommand)
                    verify(exactly = 1) { adminRepository.save(any()) }
                }
            }
            context("Admin이 이미 존재하면") {
                every { adminRepository.existsByEmail(registerAdminCommand.email) } returns true
                it("예외를 던진다") {
                    val exception =
                        shouldThrow<IllegalStateException> { adminService.registerAdmin(registerAdminCommand) }
                    exception.message shouldBe "이미 가입된 어드민입니다."
                }
            }
        }

        describe("비밀번호가 없는 Admin 생성 요청") {
            val registerAdminWithoutPasswordCommand: RegisterAdminWithoutPasswordCommand = RegisterAdminWithoutPasswordCommand(
                organization = "organizationA",
                name = "adminB",
                email = "emailB",
                authority = "멤버",
            )
            context("Admin이 이미 존재하지 않으면") {
                every { adminRepository.existsByEmail(registerAdminWithoutPasswordCommand.email) } returns false
                every { adminRepository.save(any()) } returns admin
                it("Admin을 생성한다") {
                    adminService.registerAdminWithoutPassword(registerAdminWithoutPasswordCommand, organization)
                    verify(exactly = 1) { adminRepository.save(any()) }
                }
            }
            context("Admin이 이미 존재하면") {
                every { adminRepository.existsByEmail(registerAdminWithoutPasswordCommand.email) } returns true
                it("예외를 던진다") {
                    val exception =
                        shouldThrow<IllegalStateException> {
                            adminService.registerAdminWithoutPassword(
                                registerAdminWithoutPasswordCommand,
                                organization,
                            )
                        }
                    exception.message shouldBe "이미 가입된 어드민입니다."
                }
            }
        }

        describe("Admin 삭제 요청") {
            context("Admin이 있으면") {
                every { adminRepository.findByIdOrNull(admin1.id) } returns admin1
                every { adminRepository.deleteById(admin1.id) } returns Unit
                it("해당 Admin이 삭제됨") {
                    adminService.deleteAdmin(admin1.id)
                    verify(exactly = 1) { adminRepository.deleteById(admin1.id) }
                }
            }
            context("Admin이 없으면") {
                every { adminRepository.findByIdOrNull(admin1.id) } returns null
                every { adminRepository.deleteById(admin1.id) } throws IllegalStateException()
                it("예외를 반환함") {
                    val exception = shouldThrow<IllegalStateException> { adminService.deleteAdmin(admin1.id) }
                    println(exception)
                }
            }
        }
    })
