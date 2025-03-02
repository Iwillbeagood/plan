package jun.money.mate.database.converter

import androidx.room.TypeConverter
import jun.money.mate.model.etc.DateType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate

class DateTypeConverter {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromDateType(value: DateType): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toDateType(value: String): DateType {
        return json.decodeFromString(value)
    }

    @TypeConverter
    fun fromLocalDate(value: LocalDate): String {
        return value.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }
}
