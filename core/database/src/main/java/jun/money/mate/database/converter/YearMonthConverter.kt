package jun.money.mate.database.converter

import androidx.room.TypeConverter
import java.time.YearMonth
import java.time.format.DateTimeFormatter

internal class YearMonthConverter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM") // "YYYY-MM" 형식

    @TypeConverter
    fun fromYearMonth(yearMonth: YearMonth?): String? {
        return yearMonth?.format(formatter)
    }

    @TypeConverter
    fun toYearMonth(yearMonthString: String?): YearMonth? {
        return yearMonthString?.let {
            YearMonth.parse(it, formatter)
        }
    }
}
