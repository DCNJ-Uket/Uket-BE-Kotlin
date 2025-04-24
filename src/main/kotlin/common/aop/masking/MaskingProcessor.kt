package uket.common.aop.masking

import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object MaskingProcessor {
    fun <T : Any> applyMasking(dto: T): T {
        val dtoClass = dto::class
        val constructor = dtoClass.primaryConstructor ?: return dto

        val args = constructor.parameters.map { parameter ->
            val property = dtoClass.memberProperties.find { it.name == parameter.name } ?: return@map null
            val originalValue = property.getter.call(dto)
            val maskedValue = property.findAnnotation<Mask>()?.let {
                if (originalValue is String) MaskingUtil.maskingOf(it.type, originalValue) else originalValue
            } ?: originalValue
            maskedValue
        }.toTypedArray()

        return constructor.call(*args)
    }
}
