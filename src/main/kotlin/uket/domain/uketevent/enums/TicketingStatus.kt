package uket.domain.uketevent.enums

enum class TicketingStatus(
    val order: Int,
) {
    티켓팅_진행중(order = 1),
    오픈_예정(order = 2),
    티켓팅_종료(order = 3),
}
