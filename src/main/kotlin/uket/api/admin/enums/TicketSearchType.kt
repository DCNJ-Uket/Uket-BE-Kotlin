package uket.api.admin.enums

enum class TicketSearchType {
    STATUS,
    USER_NAME,
    PHONE_NUMBER,
    SHOW_DATE,
    CREATED_AT,
    MODIFIED_AT,
    NONE,
    ;

    companion object {
        val default: TicketSearchType
            get() = NONE
    }
}
