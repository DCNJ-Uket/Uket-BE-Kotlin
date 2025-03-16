package uket.infra.output

import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import uket.infra.output.persistence.entity.user.UserEntity

@DataJpaTest
class SampleTest {

    @Autowired
    lateinit var em : EntityManager

    @Test
    @Transactional
    fun test() {
        val user = UserEntity(null, 0L, "nameA", "emailA", "", "", "", false)
        val id = user.id
        em.persist(user)

        val findUser = em.find(UserEntity::class.java, id)
        Assertions.assertThat(findUser.name).isEqualTo(user.name)
    }

}
