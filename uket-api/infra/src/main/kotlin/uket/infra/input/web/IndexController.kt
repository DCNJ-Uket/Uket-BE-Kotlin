package uket.infra.input.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import uket.application.usecase.TestUseCase

@RestController
class IndexController(
    private val testUseCase: TestUseCase,
) {
    @GetMapping("/index")
    fun index(): String {
        return "ok"
    }
}
