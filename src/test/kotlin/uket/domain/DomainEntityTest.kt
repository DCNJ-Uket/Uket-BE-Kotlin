package uket.domain

import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import uket.domain.user.entity.User
import uket.domain.user.enums.Platform

@DataJpaTest
class DomainEntityTest {
    @Autowired
    lateinit var em: EntityManager

    @Test
    @DisplayName("테스트 디비와 연동이 잘 되는 지 확인")
    fun testDBIntegration() {
        // given
        val user = User(
            0L,
            Platform.KAKAO,
            "platformIdA",
            "nameA",
            "emailA",
            "profileImageA",
            "depositorNameA",
            "phoneNumberA",
            true
        )
        em.persist(user)

        // when
        val findUser = em.find(User::class.java, user.id)

        // then
        Assertions.assertThat(findUser.id).isEqualTo(user.id)
    }
}
