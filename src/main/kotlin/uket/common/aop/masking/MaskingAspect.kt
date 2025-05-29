package uket.common.aop.masking

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import uket.common.response.CustomPageResponse

@Aspect
@Component
class MaskingAspect {
    @Around("execution(* uket.api..*Controller.*(..))")
    fun maskReturnValue(joinPoint: ProceedingJoinPoint): Any? {
        val result = joinPoint.proceed()
        if (result is ResponseEntity<*>) {
            val maskedBody = maskDtoIfNeeded(result.body)
            return ResponseEntity.status(result.statusCode).body(maskedBody)
        }
        return result
    }

    private fun maskDtoIfNeeded(dto: Any?): Any? {
        if (dto == null) return null
        val dtoClass = dto::class
        if (!dtoClass.isData) return dto

        return try {
            MaskingProcessor.applyMasking(dto)
        } catch (e: Exception) {
            dto
        }
    }
}
