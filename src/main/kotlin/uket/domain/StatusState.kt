package uket.uket.domain

import uket.common.PublicException

interface StatusState<T : Enum<T>, Entity> {
    val status: T
    val allowedPrevStatus: Set<T>

    fun isSupport(status: T): Boolean = this.status == status

    fun invoke(id: Long, currentStatus: T): Entity {
        if (currentStatus !in allowedPrevStatus) {
            throw PublicException(
                publicMessage = "변경할 수 없는 상태입니다.",
                systemMessage = "[${this::class.simpleName}] ${status}는 현재상태($currentStatus)에서 변경될 수 없는 상태입니다."
            )
        }
        execute(id)
        return updateStatus(id)
    }

    fun execute(id: Long)

    fun updateStatus(id: Long): Entity
}
