package uket.domain.uketevent.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(
    name = "banner",
    indexes = [Index(name = "index_banner_01", columnList = "uketEventId")]
)
class Banner(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "uket_event_id")
    var uketEventId: Long,

    @Column(name = "image_id")
    val imageId: Long,

    @Column(name = "link")
    val link: String,
)
