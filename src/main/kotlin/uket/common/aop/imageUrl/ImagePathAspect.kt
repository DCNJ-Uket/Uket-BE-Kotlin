package uket.common.aop.imageUrl

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import uket.common.response.ListResponse

@Aspect
@Component
class ImagePathAspect {
    @Value("\${app.s3.deploy-url}")
    lateinit var deployPath: String

    @Around("execution(* uket.api..*Controller.*(..))")
    fun imageUrlReturnValue(joinPoint: ProceedingJoinPoint): Any? {
        val result = joinPoint.proceed()

        if (result is ResponseEntity<*> && result.body != null) {
            val body = result.body!!

            when (body) {
                is ListResponse<*> -> {
                    body.items.forEach { changeImageIdToUrl(it!!) }
                }
                else -> changeImageIdToUrl(body)
            }
        }

        return result
    }

    private fun changeImageIdToUrl(body: Any) {
        body::class.java.declaredFields.forEach { prop ->
            if (prop.isAnnotationPresent(ImagePath::class.java)) {
                prop.isAccessible = true
                val value = prop.get(body)
                prop.set(body, "$deployPath$value")
            }
        }
    }
}
