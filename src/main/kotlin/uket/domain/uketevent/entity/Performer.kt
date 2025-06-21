package uket.domain.uketevent.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Performer(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long,

    @ManyToOne
    @JoinColumn(name = "uket_event_round_id")
    private val uketEventRound: UketEventRound,

    private val name: String,

    private var totalTicketCount: Int,
) {
    fun addTicketCount(addCount: Int) {
        totalTicketCount += addCount
    }

    fun minusTicketCount(minusCount: Int) {
        totalTicketCount -= minusCount
    }
}
