package jun.money.mate.database.converter

import androidx.room.TypeConverter
import jun.money.mate.model.income.IncomeType

class IncomeTypeConverter {

    @TypeConverter
    fun fromIncomeType(incomeType: IncomeType): String {
        return incomeType.name
    }

    @TypeConverter
    fun toIncomeType(value: String): IncomeType {
        return IncomeType.valueOf(value)
    }
}
