package uket.domain.uketevent.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class ListToStringConverter : AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(list: List<String>?): String = list?.joinToString().orEmpty()

    override fun convertToEntityAttribute(data: String?): List<String> = data?.split(",") ?: emptyList()
}
