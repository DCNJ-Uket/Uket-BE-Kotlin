package uket.auth.config.adminId

import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginAdminId()
