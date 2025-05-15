package uket.domain.user

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import uket.domain.user.dto.CreateUserCommand
import uket.domain.user.enums.Platform
import uket.domain.user.repository.UserRepository
import uket.domain.user.service.UserService
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class UserServiceTest(
    val userService: UserService,
    val userRepository: UserRepository,
) : DescribeSpec({

        isolationMode = IsolationMode.InstancePerLeaf

        describe("createUser") {
            context("10개의 요청이 있을 때") {
                val executorService = Executors.newFixedThreadPool(10)
                val countDownLatch = CountDownLatch(10)
                var count = 0
                it("동시 요청") {
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
                            } catch (e: DataIntegrityViolationException) {
                                println(e)
                                println("unique constraints violation")
                                count += 1
                            } finally {
                                countDownLatch.countDown()
                            }
                        })
                    }
                    countDownLatch.await()

                    val users = userRepository.findAll()
                    users.size shouldBe 1
                    count shouldBeGreaterThanOrEqual 1
                }
                it("500ms 간격 요청") {
                    for (i in 1..10) {
                        executorService.submit({
                            try {
                                Thread.sleep(i * 500L)
                                userService.createUser(
                                    CreateUserCommand(
                                        Platform.KAKAO,
                                        "1234",
                                        "name",
                                        "email",
                                        "profileImage"
                                    )
                                )
                            } catch (e: DataIntegrityViolationException) {
                                println(e)
                                println("unique constraints violation")
                                count += 1
                            } finally {
                                countDownLatch.countDown()
                            }
                        })
                    }
                    countDownLatch.await()

                    val users = userRepository.findAll()
                    users.size shouldBe 1
                    count shouldBe 0
                }
            }
        }
    })
