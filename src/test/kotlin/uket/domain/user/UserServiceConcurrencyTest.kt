package uket.domain.user

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import uket.common.PublicException
import uket.domain.user.dto.CreateUserCommand
import uket.domain.user.enums.Platform
import uket.domain.user.repository.UserRepository
import uket.domain.user.service.UserService
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class UserServiceConcurrencyTest(
    val userService: UserService,
    val userRepository: UserRepository,
) : DescribeSpec({

        isolationMode = IsolationMode.InstancePerLeaf

        beforeEach {
            userRepository.deleteAll()
        }

        describe("createUser") {
            context("10개의 요청이 있을 때") {
                val executorService = Executors.newFixedThreadPool(10)
                val countDownLatch = CountDownLatch(10)
                it("동시 요청") {
                    var errorCount = 0
                    for (i in 1..10) {
                        executorService.submit({
                            try {
                                userService.createUser(
                                    CreateUserCommand(
                                        Platform.KAKAO,
                                        "1234",
                                        "name",
                                        "email",
                                        "profileImage"
                                    )
                                )
                            } catch (e: PublicException) {
                                errorCount += 1
                            } finally {
                                countDownLatch.countDown()
                            }
                        })
                    }
                    countDownLatch.await()

                    val users = userRepository.findAll()
                    users.size shouldBe 1
                    errorCount shouldBeGreaterThanOrEqual 0
                    println("Error Count : $errorCount")
                }
                it("연달아 요청") {
                    var errorCount = 0
                    for (i in 1..10) {
                        try {
                            userService.createUser(
                                CreateUserCommand(
                                    Platform.KAKAO,
                                    "1234",
                                    "name",
                                    "email",
                                    "profileImage"
                                )
                            )
                        } catch (e: PublicException) {
                            println("unique constraints violation")
                            errorCount += 1
                        }
                    }

                    val users = userRepository.findAll()
                    users.size shouldBe 1
                    errorCount shouldBe 9
                }
            }
        }
    })
