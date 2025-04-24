package uket.api.admin.enums

enum class TicketSearchType {
    STATUS,
    USER_NAME,
    PHONE_NUMBER,
    SHOW_DATE,
    RESERVATION_USER_TYPE,
    CREATED_AT,
    MODIFIED_AT,
    NONE;

    companion object  {
        val default: TicketSearchType
            get() = NONE
    }
}
