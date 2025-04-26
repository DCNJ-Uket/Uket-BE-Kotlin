package uket.common

inline fun <reified T : Enum<T>> String.toEnum(): T {
    val enumValue = this.replace(" ", "_")

    return enumValues<T>().find { it.name.equals(enumValue, ignoreCase = true) }
        ?: throw IllegalArgumentException("${T::class.simpleName} 으로의 변환에 실패했습니다. | value: $this")
}

inline fun <reified T : Enum<T>> T.toResponseFormat(): String {
    return this.name.replace("_", " ")
}
