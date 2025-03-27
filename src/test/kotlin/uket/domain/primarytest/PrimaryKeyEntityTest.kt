package uket.domain.primarytest

import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import uket.uket.QueryDslConfig
import uket.uket.domain.primarykeys.PrimaryKeyUsers
import uket.uket.domain.user.enums.Platform

@DataJpaTest
@EntityScan(basePackages = [ "uket" ])
@Import(QueryDslConfig::class)
class PrimaryKeyEntityTest {
    @Autowired
    lateinit var em: EntityManager

    @Test
    @DisplayName("영속화 이후 조회가 잘 되는 지 확인")
    fun testFindAfterPersist() {
        // given
        val users = PrimaryKeyUsers(Platform.KAKAO, 0L, "nameA", "emailA", null, "depositorNameA", "phoneNumberA", true)
        em.persist(users)

        // when
        val findUsers = em.find(PrimaryKeyUsers::class.java, users.id)

        // then
        Assertions.assertThat(findUsers.id).isEqualTo(users.id)
    }

    @Test
    @DisplayName("객체 생성 이후, 영속화 전, ID 값이 null이 아님을 확인")
    fun testCheckIDAfterCreationBeforePersist() {
        // given
        val users = PrimaryKeyUsers(Platform.KAKAO, 0L, "nameA", "emailA", null, "depositorNameA", "phoneNumberA", true)

        // when

        // then
        Assertions.assertThat(users.id).isNotEqualTo(null)
    }
}
