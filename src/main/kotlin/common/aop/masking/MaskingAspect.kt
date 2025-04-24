package uket.common.aop.masking

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import uket.common.response.CustomPageResponse

@Aspect
@Component
class MaskingAspect {

    @Around("execution(* uket.api.*Controller.*(..))")
    fun maskReturnValue(joinPoint: ProceedingJoinPoint): Any? {
        val result = joinPoint.proceed()
        return when (result) {
            is CustomPageResponse<*> -> {
                val maskedContent = result.content.mapNotNull { maskDtoIfNeeded(it) }
                CustomPageResponse(
                    content = maskedContent,
                    pageNumber = result.pageNumber,
                    pageSize = result.pageSize,
                    first = result.first,
                    last = result.last,
                    totalElements = result.totalElements,
                    totalPages = result.totalPages,
                    empty = result.empty
                )
            }
            else -> maskDtoIfNeeded(result)
        }
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

