package uket.uket.domain.eventregistration.entity

enum class EventRegistrationStatus(
    val order: Int,
    val isModifiable: Boolean = false,
) {
    /**
     * 사용자가 티켓 정보 제출을 완료했을 때 나타나는 표시
     */
    검수진행(order = 1, isModifiable = true),

    /**
     * 관리자가 해당 티켓을 검수 완료했을 때 나타나는 표시
     * - 이 시점부터 주최측이 수정 불가능함
     */
    검수완료(order = 2),

    /**
     * 관리자/시스템이 해당 티켓을 서비스에 등록 완료했을 때 나타나는 표시
     * - UketEvent 생성됨
     */
    등록완료(order = 3),

    /**
     * 티켓 구매 기간이 종료되었을 때 나타나는 표시
     */
    행사완료(order = 4),

    /**
     * 관리자가 해당 티켓을 취소 처리했을 때 나타나는 표시
     */
    등록취쇼(order = 99),
}
