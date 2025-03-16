package uket.application.service

import org.springframework.stereotype.Component
import uket.application.port.TestPort
import uket.application.usecase.TestUseCase

@Component
internal class TestService(
    private val testPort: TestPort
): TestUseCase {

}
