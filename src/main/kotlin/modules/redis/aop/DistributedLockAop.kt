package uket.modules.redis.aop

import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import uket.modules.redis.util.CustomSpringELParser

private val log = KotlinLogging.logger {}

@Aspect
@Component
class DistributedLockAop(
    private val redissonClient: RedissonClient,
    private val aopForTransaction: AopForTransaction,
) {
    companion object {
        private const val REDISSON_LOCK_PREFIX = "LOCK:"
    }

    @Around("@annotation(uket.modules.redis.aop.DistributedLock)")
    @Throws(Throwable::class)
    fun lock(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val annotation = method.getAnnotation(DistributedLock::class.java)

        val key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
            signature.parameterNames,
            joinPoint.args,
            annotation.key,
        )

        val lock: RLock = redissonClient.getLock(key.toString())

        try {
            val available = lock.tryLock(
                annotation.waitTime,
                annotation.leaseTime,
                annotation.timeUnit,
            )

            if (!available) {
                return false
            }
            return aopForTransaction.proceed(joinPoint)
        } catch (e: InterruptedException) {
            throw InterruptedException()
        } finally {
            try {
                lock.unlock()
            } catch (e: IllegalMonitorStateException) {
                log.info(
                    "Redisson Lock Already UnLock {} {}",
                    keyValue("serviceName", method.name),
                    keyValue("key", key.toString()),
                )
            }
        }
    }

    private fun keyValue(key: String, value: String): String {
        return "$key:$value"
    }
}
