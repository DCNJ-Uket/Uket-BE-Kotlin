package uket.domain.eventregistration.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class ListToStringConverter : AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(list: List<String>?): String {
        return list?.joinToString().orEmpty()
    }

    override fun convertToEntityAttribute(data: String?): List<String> {
        return data?.split(",") ?: emptyList()
    }
}
