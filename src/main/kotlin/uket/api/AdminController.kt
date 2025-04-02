package uket.uket.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController {
    @GetMapping("/api/v1/admin/test")
    fun test(): String {
        return "test"
    }
}
