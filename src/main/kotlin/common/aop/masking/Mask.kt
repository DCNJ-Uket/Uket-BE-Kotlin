package uket.common.aop.masking


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Mask(val type: MaskingType)
