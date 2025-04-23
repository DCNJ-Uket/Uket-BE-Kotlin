package uket.domain.images

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import uket.domain.BaseTimeEntity

@Entity
class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
): BaseTimeEntity() {
}
