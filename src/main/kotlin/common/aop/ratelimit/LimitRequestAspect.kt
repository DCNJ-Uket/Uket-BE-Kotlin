package uket.common.aop.ratelimit

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import uket.common.PublicException
import java.time.Duration

@Aspect
@Component
class LimitRequestAspect() {
    private val bucket: Bucket = Bucket.builder()
        .addLimit(Bandwidth.simple(100, Duration.ofMinutes(1)))
        .build()

    @Before("@annotation(uket.common.aop.ratelimit.LimitRequest)")
    fun limitRequest() {
        if (!bucket.tryConsume(1)) {
            throw PublicException()
        }
    }


}
