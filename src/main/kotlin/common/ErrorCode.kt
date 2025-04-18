package uket.common

enum class ErrorCode(
    val status: Int,
    val code: String,
    val message: String,
) {
    /**
     * Common Errors
     */
    UNKNOWN_SERVER_ERROR(500, "CM0001", "일시적으로 접속이 원활하지 않습니다. 서버 팀에 문의 부탁드립니다."),
    INVALID_INPUT_VALUE(400, "CM0002", "유효하지 않은 입력입니다."),
    INVALID_DATE(400, "CM0003", "유효하지 않은 연 또는 월입니다."),
    METHOD_NOT_ALLOWED(405, "CM0004", "허가되지 않은 메서드입니다."),
    IO_ERROR(500, "CM0005", "I/O 관련 에러입니다. 서버 팀에 문의 부탁드립니다."),
    INVALID_ROLE(400, "CM0006", "권한이 유효하지 않습니다."),

    /**
     * Auth Related Errors
     */
    NOT_MATCH_CATEGORY(400, "AU0001", "올바르지 않은 유형의 토큰입니다."),
    FAIL_REQUEST_TO_OAUTH2(400, "AU0002", "OAuth2 요청이 실패했습니다."),
    AUTHENTICATION_FAILED(401, "AU0003", "인증에 실패하였습니다."),
    TOKEN_AUTHENTICATION_FAILED(401, "AU0004", "토큰 인증에 실패하였습니다."),
    INVALID_TOKEN(401, "AU0005", "유효하지 않은 토큰입니다."),
    INVALID_PLATFORM(400, "AU0006", "유효하지 않은 플랫폼입니다."),
    AUTHORIZATION_FAILED(403, "AU0007", "접근 권한이 없습니다."),
    TOKEN_EXPIRED(403, "AU0008", "만료된 토큰입니다."),
    TOKEN_NOT_EXPIRED(403, "AU0009", "아직 토큰이 만료되지 않았습니다."),
    UNABLE_TO_SEND_EMAIL(400, "AU0010", "이메일 전송에 실패했습니다."),
    INVALID_AUTH_CODE(400, "AU0011", "유효하지 않은 인증 코드입니다."),
    NOT_MATCHED_AUTH_CODE(400, "AU0012", "올바르지 않은 인증 코드입니다."),

    /**
     * Organization Errors
     */
    NOT_FOUND_EMAIL_OF_ADMIN(404, "OR0001", "어드민의 이메일을 찾을 수 없습니다."),
    NOT_MATCH_PASSWORD(400, "OR0002", "올바르지 않은 비밀번호입니다."),
    ALREADY_EXIST_ADMIN(400, "OR0003", "이미 가입된 어드민입니다."),
    NOT_REGISTERED_ADMIN(400, "OR0004", "인가되지 않은 어드민입니다."),
    NOT_FOUND_ORGANIZATION(404, "OR0005", "단체를 찾을 수 없습니다."),

    /**
     * User Errors
     */
    NOT_FOUND_USER(404, "US0001", "해당 사용자를 찾을 수 없습니다."),
    ALREADY_EXIST_USER(400, "US0002", "이미 가입된 사용자입니다."),
    NOT_REGISTERED_USER(400, "US0003", "가입되지 않은 사용자입니다. 회원가입 후 이용해주세요"),
    NOT_FOUND_USER_DETAILS(404, "US0004", "해당 사용자 세부정보를 찾을 수 없습니다."),
    ALREADY_REGISTERED_USER(400, "US0005", "이미 가입된 사용자입니다."),

    /**
     * UketEvent Errors
     */
    NOT_FOUND_UKET_EVENT(404, "UK0001", "해당 행사를 찾을 수 없습니다."),
    NOT_FOUND_UKET_EVENT_ROUND(404, "UK0002", "해당 회차를 찾을 수 없습니다."),
    NOT_FOUND_ENTRY_GROUP(404, "UK0003", "해당 입장 그룹을 찾을 수 없습니다."),
    FAIL_RESERVATION_COUNT(400, "UK0004", "해당 입장 그룹의 예매 가능 인원이 없습니다."),
}
