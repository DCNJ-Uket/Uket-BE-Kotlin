package uket.domain.uketevent.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Performer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "uket_event_round_id")
    val uketEventRoundId: Long,

    val name: String,

    var totalTicketCount: Int,
) {
    fun addTicketCount(addCount: Int) {
        totalTicketCount += addCount
    }

    fun minusTicketCount(minusCount: Int) {
        totalTicketCount -= minusCount
    }
}
