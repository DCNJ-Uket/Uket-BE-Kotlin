package uket.domain

import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import uket.uket.domain.user.Platform
import uket.uket.domain.user.Users

@DataJpaTest
class DomainEntityTest {
    @Autowired
    lateinit var em: EntityManager

    @Test
    @DisplayName("테스트 디비와 연동이 잘 되는 지 확인")
    fun testDBIntegration() {
        // given
        val users = Users(0L, Platform.KAKAO, 0L, "nameA", "emailA", null, "depositorNameA", "phoneNumberA", true)
        em.persist(users)

        // when
        val findUsers = em.find(Users::class.java, users.id)

        // then
        Assertions.assertThat(findUsers.id).isEqualTo(users.id)
    }
}
