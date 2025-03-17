package uket.uket.auth.jwt

object JwtValues {
    const val JWT_PAYLOAD_KEY_CATEGORY: String = "category"
    const val JWT_PAYLOAD_KEY_ID: String = "id"
    const val JWT_PAYLOAD_KEY_NAME: String = "name"
    const val JWT_PAYLOAD_KEY_ROLE: String = "role"
    const val JWT_PAYLOAD_KEY_REGISTERED: String = "isRegistered"
    const val JWT_PAYLOAD_KEY_UUID: String = "uuid"
    const val JWT_AUTHORIZATION_HEADER: String = "Authorization"
    const val JWT_AUTHORIZATION_VALUE_PREFIX: String = "Bearer "
    const val JWT_PAYLOAD_VALUE_ACCESS: String = "access"
    const val JWT_PAYLOAD_VALUE_REFRESH: String = "refresh"
}
