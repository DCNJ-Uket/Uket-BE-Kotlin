package uket.domain.admin.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
        val admin: Admin = Admin(
            organization = organization,
            name = "adminA",
            email = "emailA",
            password = "passwordA",
            isSuperAdmin = false,
        )

        describe("Admin을 ID로 조회") {

            context("Admin이 있으면") {
                every { adminRepository.findByIdOrNull(admin.id) } returns admin

                it("해당 Admin을 반환한다") {
                    val findAdmin = adminService.findById(admin.id)
                    admin.shouldNotBeNull()
                    findAdmin.name shouldBe admin.name
                }
            }

            context("Admin이 없으면") {
                every { adminRepository.findByIdOrNull(admin.id) } returns null

                it("예외를 던진다") {
                    val exception = shouldThrow<IllegalStateException> { adminService.findById(admin.id) }
                    exception.message shouldBe "해당 어드민을 찾을 수 없습니다"
                }
            }
        }

        describe("Admin을 email로 조회") {
            context("Admin이 있으면") {
                every { adminRepository.findByEmail(admin.email) } returns admin
                it("해당 Admin을 반환한다") {
                    val findAdmin = adminService.findByEmail(admin.email)
                    admin.shouldNotBeNull()
                    findAdmin.email shouldBe admin.email
                }
            }
            context("Admin이 없으면") {
                every { adminRepository.findByEmail(admin.email) } returns null
                it("예외를 던진다") {
                    val exception = shouldThrow<IllegalStateException> { adminService.findByEmail(admin.email) }
                    exception.message shouldBe "해당 어드민을 찾을 수 없습니다"
                }
            }
        }

        describe("Admin 생성 요청") {
            val registerAdminCommand: RegisterAdminCommand = RegisterAdminCommand(
                organization = organization,
                name = "adminB",
                email = "emailB",
                password = "passwordB",
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
                    val exception = shouldThrow<IllegalStateException> { adminService.registerAdmin(registerAdminCommand) }
                    exception.message shouldBe "이미 가입된 어드민입니다."
                }
            }
        }

        describe("비밀번호가 없는 Admin 생성 요청") {
            val registerAdminWithoutPasswordCommand: RegisterAdminWithoutPasswordCommand = RegisterAdminWithoutPasswordCommand(
                organization = organization,
                name = "adminB",
                email = "emailB",
            )
            context("Admin이 이미 존재하지 않으면") {
                every { adminRepository.existsByEmail(registerAdminWithoutPasswordCommand.email) } returns false
                every { adminRepository.save(any()) } returns null
                it("Admin을 생성한다") {
                    adminService.registerAdminWithoutPassword(registerAdminWithoutPasswordCommand)
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
                            )
                        }
                    exception.message shouldBe "이미 가입된 어드민입니다."
                }
            }
        }

        describe("Admin 삭제 요청") {
            context("Admin이 있으면") {
                every { adminRepository.deleteById(admin.id) } returns Unit
                it("해당 Admin이 삭제됨") {
                    adminService.deleteAdmin(admin.id)
                    verify(exactly = 1) { adminRepository.deleteById(admin.id) }
                }
            }
            context("Admin이 없으면") {
                every { adminRepository.deleteById(admin.id) } throws IllegalStateException()
                it("예외를 반환함") {
                    val exception = shouldThrow<IllegalStateException> { adminService.deleteAdmin(admin.id) }
                    println(exception)
                }
            }
        }
    })
